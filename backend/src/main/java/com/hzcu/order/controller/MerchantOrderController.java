package com.hzcu.order.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.model.Order;
import com.hzcu.order.service.OrderService;

@RestController
@CrossOrigin
public class MerchantOrderController {

  private final OrderService orderService;

  public MerchantOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/merchant/orders/{orderId}/complete")
  public ApiResponse<Order> complete(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.complete(orderId));
  }

  @PostMapping("/merchant/orders/{orderId}/ready")
  public ApiResponse<Order> ready(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.ready(orderId));
  }
}
