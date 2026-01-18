package com.hzcu.order.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WechatNotificationService {
    private static final Logger log = LoggerFactory.getLogger(WechatNotificationService.class);

    public void sendSubscriptionMessage(String openid, String templateId, Map<String, Object> data) {
        log.info("Sending WeChat subscription message to {}: {}", openid, templateId);

        // Mock implementation
        log.info("Message data: {}", data);
        log.info("Message sent successfully (mocked)");
    }
}
