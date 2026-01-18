package com.hzcu.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hzcu.order.entity.Dish;
import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.OrderItem;
import com.hzcu.order.entity.OrderStatusLog;
import com.hzcu.order.repository.DishRepository;
import com.hzcu.order.repository.OrderItemRepository;
import com.hzcu.order.repository.OrderRepository;
import com.hzcu.order.repository.OrderStatusLogRepository;

public class OrderServiceWhiteBoxTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderStatusLogRepository orderStatusLogRepository;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("ORD-TC-01: 完整数据路径测试")
    void createOrder_CompleteData_Success() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setDishId(1L);
        dish.setName("红烧肉");

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("25.00"));
        item.setTotalPrice(new BigDecimal("25.00"));

        List<OrderItem> items = List.of(item);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrder(order, items);

        assertEquals("PENDING_PAYMENT", result.getStatus());
        assertEquals("红烧肉", items.get(0).getDishName());
        verify(dishRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("ORD-TC-02: 菜品名称补全路径测试")
    void createOrder_DishNameMissing_QueriesDB() {
        Order order = new Order();
        Dish dishInput = new Dish();
        dishInput.setDishId(1L);
        dishInput.setName(null); // 名称缺失

        Dish dishInDB = new Dish();
        dishInDB.setDishId(1L);
        dishInDB.setName("宫保鸡丁");

        OrderItem item = new OrderItem();
        item.setDish(dishInput);
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("15.00"));
        item.setTotalPrice(new BigDecimal("30.00"));

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dishInDB));

        orderService.createOrder(order, List.of(item));

        assertEquals("宫保鸡丁", item.getDishName());
        verify(dishRepository).findById(1L);
    }

    @Test
    @DisplayName("ORD-TC-03: 金额计算补全路径测试")
    void createOrder_TotalPriceMissing_Calculates() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setDishId(1L);
        dish.setName("青菜");

        OrderItem item = new OrderItem();
        item.setDish(dish);
        item.setQuantity(3);
        item.setUnitPrice(new BigDecimal("5.00"));
        item.setTotalPrice(null); // 总价缺失

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.createOrder(order, List.of(item));

        assertEquals(new BigDecimal("15.00"), item.getTotalPrice());
    }

    @Test
    @DisplayName("ORD-TC-04: 日志完整性校验")
    void createOrder_LogsStatusChange() {
        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.createOrder(order, new ArrayList<>());

        verify(orderStatusLogRepository).save(any(OrderStatusLog.class));
    }

    @Test
    @DisplayName("ORD-TC-05: 空订单项场景")
    void createOrder_EmptyItems_Success() {
        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> {
            Order result = orderService.createOrder(order, new ArrayList<>());
            assertNotNull(result);
            verify(orderItemRepository, never()).save(any());
        });
    }
}
