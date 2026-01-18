package com.hzcu.order.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.entity.User;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User profile and balance APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ApiResponse<User> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        return userService.findById(currentUser.getId())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "User not found"));
    }

    @PostMapping("/recharge")
    @Operation(summary = "Recharge user balance")
    public ApiResponse<User> recharge(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody Map<String, BigDecimal> data) {
        
        BigDecimal amount = data.get("amount");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ApiResponse.error(400, "Invalid recharge amount");
        }
        
        User user = userService.recharge(currentUser.getId(), amount);
        return ApiResponse.success(user);
    }
}
