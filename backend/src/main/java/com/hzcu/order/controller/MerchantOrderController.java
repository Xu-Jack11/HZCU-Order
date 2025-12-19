package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.dto.EntityMapper;
import com.hzcu.order.dto.OrderDTO;
import com.hzcu.order.entity.Order;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.service.CanteenService;
import com.hzcu.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/merchant/orders")
@Tag(name = "Merchant Order", description = "Merchant-side order management APIs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CanteenService canteenService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all orders for the merchant's canteen")
    public ApiResponse<List<OrderDTO>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String status) {

        return canteenService.getCanteenById(currentUser.getCanteenId())
                .map(canteen -> {
                    List<Order> orders;
                    if (status != null) {
                        orders = orderService.getOrdersByCanteenAndStatus(canteen, status);
                    } else {
                        orders = orderService.getOrdersByCanteen(canteen);
                    }
                    List<OrderDTO> dtos = orders.stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());
                    return ApiResponse.success(dtos);
                })
                .orElse(ApiResponse.<List<OrderDTO>>error(404, "Canteen not found"));
    }

    @PatchMapping("/{id}/accept")
    @Operation(summary = "Merchant accepts order")
    public ApiResponse<String> acceptOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        return orderService.getOrderById(id)
                .map(order -> {
                    if (!"PAID".equals(order.getStatus())) {
                        return ApiResponse.<String>error(400, "Only PAID orders can be accepted");
                    }
                    orderService.updateOrderStatus(order, "PREPARING", "MERCHANT", currentUser.getId(),
                            "Merchant accepted order");
                    return ApiResponse.success("Order accepted", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Order not found"));
    }

    @PatchMapping("/{id}/finish")
    @Operation(summary = "Merchant finishes preparation")
    public ApiResponse<String> finishPreparation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        return orderService.getOrderById(id)
                .map(order -> {
                    if (!"PREPARING".equals(order.getStatus())) {
                        return ApiResponse.<String>error(400, "Only PREPARING orders can be finished");
                    }
                    orderService.updateOrderStatus(order, "READY_FOR_PICKUP", "MERCHANT", currentUser.getId(),
                            "Meal is ready for pickup");
                    return ApiResponse.success("Order ready", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Order not found"));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Merchant confirms pickup/delivery")
    public ApiResponse<String> completeOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        return orderService.getOrderById(id)
                .map(order -> {
                    orderService.updateOrderStatus(order, "COMPLETED", "MERCHANT", currentUser.getId(),
                            "Order completed");
                    return ApiResponse.success("Order completed", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Order not found"));
    }
}
