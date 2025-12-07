package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.common.PageResult;
import com.hzcu.order.dto.CreateOrderRequest;
import com.hzcu.order.model.Order;
import com.hzcu.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/orders")
  public ApiResponse<PageResult<Order>> listOrders(
      @RequestParam(defaultValue = "all") String status,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int pageSize) {
    int currentPage = Math.max(page, 1);
    int size = Math.max(pageSize, 1);
    return ApiResponse.success(orderService.listOrders(status, currentPage, size));
  }

  @PostMapping("/orders")
  public ApiResponse<Order> createOrder(@RequestBody @Valid CreateOrderRequest request) {
    return ApiResponse.success(orderService.createOrder(request));
  }

  @PostMapping("/orders/{orderId}/cancel")
  public ApiResponse<Order> cancel(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.cancel(orderId));
  }

  @PostMapping("/orders/{orderId}/pay")
  public ApiResponse<Order> pay(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.pay(orderId));
  }
}
