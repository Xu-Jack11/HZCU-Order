package com.hzcu.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendOrderStatusNotification(Long userId, String status, String orderNo) {
        String destination = "/topic/orders/" + userId;
        String message = "Your order " + orderNo + " status is now: " + status;
        messagingTemplate.convertAndSend(destination, message);
    }
}
