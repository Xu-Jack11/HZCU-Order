package com.hzcu.order.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hzcu.order.entity.Order;

@Service
public class WechatPayService {
    private static final Logger log = LoggerFactory.getLogger(WechatPayService.class);

    public Map<String, String> createUnifiedOrder(Order order) {
        log.info("Creating WeChat unified order for Order: {}", order.getOrderNo());

        Map<String, String> payParams = new HashMap<>();
        payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        payParams.put("nonceStr", "mock_nonce_" + System.currentTimeMillis());
        payParams.put("package", "prepay_id=mock_prepay_id_" + order.getOrderNo());
        payParams.put("signType", "RSA");
        payParams.put("paySign", "mock_signature");

        return payParams;
    }

    public boolean verifyNotification(Map<String, String> notifyParams) {
        return true;
    }
}
