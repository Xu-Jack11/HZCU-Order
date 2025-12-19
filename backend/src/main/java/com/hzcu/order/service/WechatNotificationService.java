package com.hzcu.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class WechatNotificationService {

    public void sendSubscriptionMessage(String openid, String templateId, Map<String, Object> data) {
        log.info("Sending WeChat subscription message to {}: {}", openid, templateId);

        // Mock implementation
        log.info("Message data: {}", data);
        log.info("Message sent successfully (mocked)");
    }
}
