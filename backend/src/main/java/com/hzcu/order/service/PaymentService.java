package com.hzcu.order.service;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.PaymentRecord;
import com.hzcu.order.repository.PaymentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private OrderService orderService;

    @Transactional
    public PaymentRecord processPayment(Order order, String channel) {
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
