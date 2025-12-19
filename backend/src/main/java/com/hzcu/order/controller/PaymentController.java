package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.service.OrderService;
import com.hzcu.order.service.PaymentService;
import com.hzcu.order.service.WechatPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment", description = "Payment processing APIs")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WechatPayService wechatPayService;

    @PostMapping("/create/{orderId}")
    @Operation(summary = "Initiate payment for an order")
    public ApiResponse<Map<String, String>> createPayment(
            @PathVariable Long orderId,
            @RequestParam String channel) {

        return orderService.getOrderById(orderId)
                .map(order -> {
                    if (channel.equalsIgnoreCase("WECHAT")) {
                        // In real world, we call WechatPay and get prepay params
                        Map<String, String> payParams = wechatPayService.createUnifiedOrder(order);

                        // For demo, we auto-process it as success if it's a mock
                        paymentService.processPayment(order, channel);

                        return ApiResponse.success(payParams);
                    }
                    return ApiResponse.<Map<String, String>>error(400, "Unsupported payment channel");
                })
                .orElse(ApiResponse.error(404, "Order not found"));
    }

    @PostMapping("/notify/wechat")
    @Operation(summary = "WeChat payment notification callback")
    public String wechatNotify(@RequestBody String xmlData) {
        // Handle WeChat notification
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
