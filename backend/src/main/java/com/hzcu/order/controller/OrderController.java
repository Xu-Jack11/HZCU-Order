package com.hzcu.order.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.common.PageResult;
import com.hzcu.order.dto.CreateOrderRequest;
import com.hzcu.order.entity.User;
import com.hzcu.order.model.Order;
import com.hzcu.order.service.OrderService;
import com.hzcu.order.service.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
public class OrderController {

  private final OrderService orderService;
  private final UserService userService;

  public OrderController(OrderService orderService, UserService userService) {
    this.orderService = orderService;
    this.userService = userService;
  }

  @GetMapping("/orders")
    public ApiResponse<PageResult<Order>> listOrders(
      @RequestParam(name = "status", defaultValue = "all") String status,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
      @RequestHeader(value = "Authorization", required = false) String authorization) {

    // 简单的token验证（实际项目中应该使用JWT或其他更安全的方式）
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return ApiResponse.error("401", "未授权访问");
    }

    String token = authorization.substring(7);
    User currentUser = userService.findByToken(token);

    if (currentUser == null) {
      return ApiResponse.error("401", "无效的认证令牌");
    }

    int currentPage = Math.max(page, 1);
    int size = Math.max(pageSize, 1);
    return ApiResponse.success(orderService.listOrdersByUser(currentUser.getUserId(), status, currentPage, size));
  }

  @PostMapping("/orders")
  public ApiResponse<Order> createOrder(@RequestBody @Valid CreateOrderRequest request, @RequestHeader(value = "Authorization", required = false) String authorization) {
    // 简单的token验证
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return ApiResponse.error("401", "未授权访问");
    }

    String token = authorization.substring(7);
    User currentUser = userService.findByToken(token);

    if (currentUser == null) {
      return ApiResponse.error("401", "无效的认证令牌");
    }

    return ApiResponse.success(orderService.createOrder(request, currentUser.getUserId()));
  }

  @PostMapping("/orders/{orderId}/cancel")
  public ApiResponse<Order> cancel(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.cancel(orderId));
  }

  @PostMapping("/orders/{orderId}/pay")
  public ApiResponse<Order> pay(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.pay(orderId));
  }

  @PostMapping("/orders/{orderId}/ready")
  public ApiResponse<Order> ready(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.ready(orderId));
  }

  @PostMapping("/orders/{orderId}/complete")
  public ApiResponse<Order> complete(@PathVariable("orderId") long orderId) {
    return ApiResponse.success(orderService.complete(orderId));
  }
}
