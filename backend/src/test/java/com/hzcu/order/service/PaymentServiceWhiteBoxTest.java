package com.hzcu.order.service;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.User;
import com.hzcu.order.repository.PaymentRecordRepository;
import com.hzcu.order.repository.UserRepository;

class PaymentServiceWhiteBoxTest {

    @Mock
    private PaymentRecordRepository paymentRecordRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("PAY-TC-01: 余额支付 - 成功扣款")
    void processPayment_BalanceSuccess() {
        User user = new User();
        user.setBalance(new BigDecimal("100.00"));

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(new BigDecimal("30.00"));
        order.setPaidAmount(new BigDecimal("30.00"));

        when(paymentRecordRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        paymentService.processPayment(order, "BALANCE");

        assertEquals(new BigDecimal("70.00"), user.getBalance());
        verify(userRepository).save(user);
        verify(orderService).updateOrderStatus(eq(order), eq("PAID"), anyString(), anyLong(), anyString());
    }

    @Test
    @DisplayName("PAY-TC-02: 余额支付 - 余额不足抛错")
    void processPayment_BalanceInsufficient_ThrowsException() {
        User user = new User();
        user.setBalance(new BigDecimal("10.00"));

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(new BigDecimal("30.00"));

        assertThrows(RuntimeException.class, () -> {
            paymentService.processPayment(order, "BALANCE");
        });

        verify(userRepository, never()).save(any());
        verify(paymentRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("PAY-TC-03: 微信支付 - 不扣除余额")
    void processPayment_Wechat_NoBalanceDeduction() {
        User user = new User();
        user.setBalance(new BigDecimal("100.00"));

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(new BigDecimal("30.00"));
        order.setPaidAmount(new BigDecimal("30.00"));

        when(paymentRecordRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        paymentService.processPayment(order, "WECHAT");

        assertEquals(new BigDecimal("100.00"), user.getBalance()); // 余额不变
        verify(userRepository, never()).save(any());
        verify(paymentRecordRepository).save(any());
    }
}
