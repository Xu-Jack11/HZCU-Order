package com.hzcu.order.service;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.OrderItem;
import com.hzcu.order.repository.OrderItemRepository;
import com.hzcu.order.repository.OrderRepository;
import com.hzcu.order.repository.OrderStatusLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

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
        OrderItem item = new OrderItem();
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
}
