package com.hzcu.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzcu.order.dto.OrderDTO;
import com.hzcu.order.dto.OrderItemDTO;
import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.User;
import com.hzcu.order.repository.OrderRepository;
import com.hzcu.order.repository.UserRepository;
import com.hzcu.order.repository.PaymentRecordRepository;
import com.hzcu.order.repository.AuditLogRepository;
import com.hzcu.order.security.CustomUserDetailsService;
import com.hzcu.order.security.JwtTokenProvider;
import com.hzcu.order.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import java.util.HashMap;
import java.util.Map;
import com.hzcu.order.repository.OrderItemRepository;
import com.hzcu.order.repository.OrderStatusLogRepository;
import com.hzcu.order.repository.DishRepository;
import com.hzcu.order.service.NotificationService;
import com.hzcu.order.entity.Dish;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FullBusinessFlowSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private OrderStatusLogRepository orderStatusLogRepository;

    @MockBean
    private PaymentRecordRepository paymentRecordRepository;

    @MockBean
    private AuditLogRepository auditLogRepository;

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private String userToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 1. 初始化模拟用户
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setOpenid("test_openid");
        testUser.setNickname("系统测试用户");
        testUser.setBalance(new BigDecimal("100.00")); // 初始余额 100

        // 2. 模拟 Security 上下文
        UserPrincipal principal = new UserPrincipal(1L, null, null, "test_user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        userToken = "Bearer " + tokenProvider.generateToken(auth);

        when(customUserDetailsService.loadUserByUsername("test_user")).thenReturn(principal);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    }

    @Test
    @DisplayName("系统流程测试：下单 -> 余额不足 -> 充值 -> 重新支付成功")
    void fullScenario_OrderRechargePay_Success() throws Exception {
        // --- 步骤 1: 尝试下单 (150元) ---
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCanteenId(1L);
        orderDTO.setDiningMode("EAT_IN");
        orderDTO.setPaymentMethod("BALANCE");
        orderDTO.setTotalAmount(new BigDecimal("150.00"));
        orderDTO.setRemark("宴请嘉宾");
        OrderItemDTO item = new OrderItemDTO();
        item.setDishId(1L);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("150.00"));
        orderDTO.setItems(Collections.singletonList(item));

        // 预期失败：初始余额只有 100
        mockMvc.perform(post("/api/v1/orders")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(containsString("余额不足")));

        // --- 步骤 2: 用户充值 100 元 ---
        // 我们不需要手动设置 balance，让 UserService.recharge 自己加
        // 但是我们需要让 userRepository.save 返回传入的对象，或者保持同一个引用
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Map<String, BigDecimal> rechargeData = new HashMap<>();
        rechargeData.put("amount", new BigDecimal("100.00"));

        mockMvc.perform(post("/api/v1/users/recharge")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rechargeData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.balance").value(200.00));

        // --- 步骤 3: 余额充足，重新下单 ---
        // 模拟菜品查询
        Dish mockDish = new Dish();
        mockDish.setDishId(1L);
        mockDish.setName("招牌套餐");
        when(dishRepository.findById(1L)).thenReturn(Optional.of(mockDish));

        Order savedOrder = new Order();
        savedOrder.setOrderId(1001L);
        savedOrder.setOrderNo("ORD202601060001");
        savedOrder.setTotalAmount(new BigDecimal("150.00"));
        savedOrder.setStatus("PENDING_PAY");
        savedOrder.setUser(testUser);

        // 模拟 Service 层依赖的所有保存操作
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any())).thenReturn(null);
        when(orderStatusLogRepository.save(any())).thenReturn(null);

        mockMvc.perform(post("/api/v1/orders")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(1001));

        // --- 步骤 4: 发起余额支付 ---
        when(orderRepository.findById(1001L)).thenReturn(Optional.of(savedOrder));
        when(paymentRecordRepository.save(any())).thenReturn(null);
        when(auditLogRepository.save(any())).thenReturn(null);

        mockMvc.perform(post("/api/v1/payment/create/1001")
                .header("Authorization", userToken)
                .param("channel", "BALANCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SUCCESS"));
    }
}
