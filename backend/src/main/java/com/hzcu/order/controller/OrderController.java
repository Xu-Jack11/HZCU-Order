package com.hzcu.order.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ApiResponse<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
        return userService.findById(currentUser.getId())
                .map(user -> {
                    List<OrderDTO> orders = orderService.getOrdersByUser(user)
                            .stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());
                    return ApiResponse.success(orders);
                })
                .orElse(ApiResponse.error(401, "User not found"));
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
