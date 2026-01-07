package com.hzcu.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.dto.EntityMapper;
import com.hzcu.order.dto.OrderDTO;
import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.OrderItem;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.service.OrderService;
import com.hzcu.order.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order", description = "Order processing APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityMapper entityMapper;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ApiResponse<OrderDTO> createOrder(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody OrderDTO orderDTO) {

        Order order = entityMapper.toEntity(orderDTO);

        com.hzcu.order.entity.User user = userService.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUser.getId()));
        
        // CHECK BALANCE if payment method is BALANCE
        if ("BALANCE".equalsIgnoreCase(orderDTO.getPaymentMethod())) {
            if (user.getBalance() == null || user.getBalance().compareTo(orderDTO.getTotalAmount()) < 0) {
                return ApiResponse.error(400, "余额不足，创建订单失败");
            }
        }

        order.setUser(user);

        // Ensure canteen is set (handled by mapper but explicit check is good)
        if (order.getCanteen() == null || order.getCanteen().getCanteenId() == null) {
            throw new RuntimeException("Canteen ID is required");
        }

        List<OrderItem> items = orderDTO.getItems().stream()
                .map(entityMapper::toEntity)
                .collect(Collectors.toList());

        Order savedOrder = orderService.createOrder(order, items);
        return ApiResponse.success(entityMapper.toDto(savedOrder));
    }

    @GetMapping
    @Operation(summary = "Get current user orders")
    public ApiResponse<List<OrderDTO>> getUserOrders(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String status) {
        return userService.findById(currentUser.getId())
                .map(user -> {
                    List<Order> orders;
                    if (status != null && !status.equalsIgnoreCase("all")) {
                        if (status.equalsIgnoreCase("preparing")) {
                            orders = orderService.getOrdersByUserAndStatuses(user, Arrays.asList("PAID", "PREPARING"));
                        } else {
                            String backendStatus = mapFrontendStatus(status);
                            orders = orderService.getOrdersByUserAndStatus(user, backendStatus);
                        }
                    } else {
                        orders = orderService.getOrdersByUser(user);
                    }
                    List<OrderDTO> dtos = orders.stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());
                    return ApiResponse.success(dtos);
                })
                .orElse(ApiResponse.error(401, "User not found"));
    }

    private String mapFrontendStatus(String status) {
        switch (status.toLowerCase()) {
            case "pending": return "PENDING_PAYMENT";
            case "preparing": return "PREPARING";
            case "ready": return "READY_FOR_PICKUP";
            case "completed": return "COMPLETED";
            case "cancelled": return "CANCELLED";
            default: return status.toUpperCase();
        }
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "User cancels order")
    public ApiResponse<String> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return orderService.getOrderById(id)
                .map(order -> {
                    if (!order.getUser().getUserId().equals(currentUser.getId())) {
                        return ApiResponse.<String>error(403, "Not authorized to cancel this order");
                    }
                    if (!"PENDING_PAYMENT".equals(order.getStatus())) {
                        return ApiResponse.<String>error(400, "Only PENDING_PAYMENT orders can be cancelled");
                    }
                    orderService.updateOrderStatus(order, "CANCELLED", "USER", currentUser.getId(), "User cancelled order");
                    return ApiResponse.success("Order cancelled", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Order not found"));
    }

    @PatchMapping("/{id}/pickup")
    @Operation(summary = "User confirms meal pickup")
    public ApiResponse<String> confirmPickup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return orderService.getOrderById(id)
                .map(order -> {
                    if (!order.getUser().getUserId().equals(currentUser.getId())) {
                        return ApiResponse.<String>error(403, "Not authorized to access this order");
                    }
                    if (!"READY_FOR_PICKUP".equals(order.getStatus())) {
                        return ApiResponse.<String>error(400, "Order is not ready for pickup");
                    }
                    orderService.updateOrderStatus(order, "COMPLETED", "USER", currentUser.getId(), "User confirmed pickup");
                    return ApiResponse.success("Enjoy your meal!", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Order not found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order details")
    public ApiResponse<OrderDTO> getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(entityMapper::toDto)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Order not found"));
    }
}
