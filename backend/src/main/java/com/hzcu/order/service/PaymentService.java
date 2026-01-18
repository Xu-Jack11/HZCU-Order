package com.hzcu.order.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.PaymentRecord;
import com.hzcu.order.repository.PaymentRecordRepository;
import com.hzcu.order.repository.UserRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PaymentRecord processPayment(Order order, String channel) {
        // Handle balance deduction if channel is BALANCE
        if ("BALANCE".equalsIgnoreCase(channel)) {
            com.hzcu.order.entity.User user = order.getUser();
            if (user.getBalance() == null || user.getBalance().compareTo(order.getTotalAmount()) < 0) {
                throw new RuntimeException("余额不足，请充值");
            }
            user.setBalance(user.getBalance().subtract(order.getTotalAmount()));
            userRepository.save(user);
        }

        // Mock payment processing
        PaymentRecord record = PaymentRecord.builder()
                .order(order)
                .payNo("PAY-" + System.currentTimeMillis())
                .channel(channel)
                .amount(order.getPaidAmount())
                .status("SUCCESS")
                .paidAt(LocalDateTime.now())
                .build();

        PaymentRecord savedRecord = paymentRecordRepository.save(record);

        // Update order status with system operator
        orderService.updateOrderStatus(order, "PAID", "SYSTEM", 0L, "Payment successful via " + channel);

        return savedRecord;
    }
}
