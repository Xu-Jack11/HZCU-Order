package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.service.AuthService;
import com.hzcu.order.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/wechat")
    @Operation(summary = "Login with WeChat", description = "Exchange WeChat info for a JWT token")
    public ApiResponse<LoginResponse> loginWechat(@RequestBody WechatLoginRequest request) {
        LoginResponse response = authService.loginWithWechatCode(
                request.getCode(),
                request.getNickname(),
                request.getAvatarUrl(),
                request.getPhoneCode());
        return ApiResponse.success("Login successful", response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ApiResponse<Void> logout() {
        // Since we use stateless JWT, we just return success.
        // Client will remove the token.
        return ApiResponse.success("Logout successful", null);
    }

    @PostMapping("/wechat/phone")
    @Operation(summary = "Bind WeChat Phone Number")
    public ApiResponse<Void> bindPhone(@RequestBody WechatPhoneRequest request) {
        // Assume the user is already authenticated via JWT
        // We'll need to get the user ID from SecurityContext
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        com.hzcu.order.security.UserPrincipal principal = (com.hzcu.order.security.UserPrincipal) authentication
                .getPrincipal();

        authService.bindPhone(principal.getId(), request.getCode());
        return ApiResponse.success("Phone number bound successfully", null);
    }

    @PostMapping("/login/merchant")
    @Operation(summary = "Login for Merchants")
    public ApiResponse<LoginResponse> loginMerchant(@RequestBody UserLoginRequest request) {
        LoginResponse response = authService.loginMerchant(request.getUsername(), request.getPassword());
        return ApiResponse.success("Merchant login successful", response);
    }

    @PostMapping("/login/admin")
    @Operation(summary = "Login for System Admins")
    public ApiResponse<LoginResponse> loginAdmin(@RequestBody UserLoginRequest request) {
        LoginResponse response = authService.loginAdmin(request.getUsername(), request.getPassword());
        return ApiResponse.success("Admin login successful", response);
    }

    @Data
    public static class WechatLoginRequest {
        private String code;
        private String nickname;
        private String avatarUrl;
        private String phoneCode;
    }

    @Data
    public static class WechatPhoneRequest {
        private String code;
    }

    @Data
    public static class UserLoginRequest {
        private String username;
        private String password;
    }
}
