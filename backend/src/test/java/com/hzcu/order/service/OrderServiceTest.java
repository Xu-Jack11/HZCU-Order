package com.hzcu.order.service;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.OrderItem;
import com.hzcu.order.entity.Dish;
import com.hzcu.order.repository.OrderItemRepository;
import com.hzcu.order.repository.OrderRepository;
import com.hzcu.order.repository.OrderStatusLogRepository;
import com.hzcu.order.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

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
    void createOrder_setsPropertiesAndSaves() {
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        
        Dish dish = Dish.builder()
                .dishId(1L)
                .name("测试菜品")
                .build();
        
        OrderItem item = OrderItem.builder()
                .dish(dish)
                .quantity(1)
                .unitPrice(new BigDecimal("10.00"))
                .totalPrice(new BigDecimal("10.00"))
                .build();
        items.add(item);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(i -> i.getArguments()[0]);

        Order saved = orderService.createOrder(order, items);

        assertNotNull(saved.getOrderNo());
        assertTrue(saved.getOrderNo().startsWith("ORD-"));
        assertEquals("PENDING_PAYMENT", saved.getStatus());
        verify(orderRepository).save(order);
        verify(orderItemRepository).save(item);
    }

    @Test
    void createOrder_withDishNameNull_queriesFromDatabase() {
        // 创建订单和订单项
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        
        // 创建一个dish对象，name为null
        Dish dishWithoutName = Dish.builder()
                .dishId(1L)
                .name(null)  // 模拟name为null的情况
                .build();
        
        // 创建完整的dish对象（从数据库查询到的）
        Dish completeDish = Dish.builder()
                .dishId(1L)
                .name("测试菜品")
                .basePrice(new BigDecimal("15.00"))
                .build();
        
        OrderItem item = OrderItem.builder()
                .dish(dishWithoutName)
                .quantity(2)
                .unitPrice(new BigDecimal("15.00"))
                .build();
        items.add(item);

        // 模拟repository行为
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(i -> i.getArguments()[0]);
        when(dishRepository.findById(1L)).thenReturn(Optional.of(completeDish));

        // 执行创建订单
        Order saved = orderService.createOrder(order, items);

        // 验证结果
        assertNotNull(saved);
        assertEquals(1, items.size());
        assertEquals("测试菜品", items.get(0).getDishName());
        
        // 验证调用了dishRepository查询
        verify(dishRepository).findById(1L);
    }

    @Test
    void createOrder_withDishNamePresent_doesNotQuery() {
        // 创建订单和订单项
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        
        // 创建一个完整的dish对象
        Dish dish = Dish.builder()
                .dishId(1L)
                .name("已存在的菜品名")
                .basePrice(new BigDecimal("20.00"))
                .build();
        
        OrderItem item = OrderItem.builder()
                .dish(dish)
                .quantity(1)
                .unitPrice(new BigDecimal("20.00"))
                .build();
        items.add(item);

        // 模拟repository行为
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(i -> i.getArguments()[0]);

        // 执行创建订单
        Order saved = orderService.createOrder(order, items);

        // 验证结果
        assertNotNull(saved);
        assertEquals(1, items.size());
        assertEquals("已存在的菜品名", items.get(0).getDishName());
        
        // 验证没有调用dishRepository查询（因为name已经存在）
        verify(dishRepository, never()).findById(any());
    }
}
