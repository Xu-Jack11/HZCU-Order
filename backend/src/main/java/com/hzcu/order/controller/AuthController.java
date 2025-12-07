package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.model.LoginResponse;
import com.hzcu.order.model.UserProfile;
import java.util.Map;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthController {

  @PostMapping("/auth/wechat/login")
  public ApiResponse<LoginResponse> login(@RequestBody Map<String, String> payload) {
    String code = payload.getOrDefault("code", "");
    String token = StringUtils.hasText(code) ? "token-" + code : "token-demo";
    LoginResponse response = new LoginResponse(token, new UserProfile(1L, "演示用户", "/images/avatar/user1.png", ""));
    return ApiResponse.success(response);
  }

  @PostMapping("/auth/logout")
  public ApiResponse<Void> logout() {
    return ApiResponse.success();
  }
}
