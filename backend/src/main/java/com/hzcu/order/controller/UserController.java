package com.hzcu.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.entity.User;
import com.hzcu.order.service.UserService;
import com.hzcu.order.service.WeChatService;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private WeChatService weChatService;

  @PostMapping("/users/login")
  public Map<String, Object> login(@RequestBody Map<String, String> body) {
    Map<String, Object> resp = new HashMap<>();
    try {
      String phone = body.get("phone");
      String nickname = body.get("nickname");
      String avatar = body.get("avatar");
      if (phone == null || phone.isBlank()) {
        resp.put("code", 400);
        resp.put("message", "phone is required");
        resp.put("data", null);
        return resp;
      }

      User user = userService.createOrUpdateByMobile(phone, nickname != null ? nickname : phone, avatar);

      // 为手机号登录用户生成token
      String token = "token_" + UUID.randomUUID().toString().replace("-", "");

      resp.put("code", 0);
      resp.put("message", "ok");
      resp.put("data", Map.of(
        "user", convertToUserProfile(user),
        "token", token
      ));
      return resp;
    } catch (Exception e) {
      resp.put("code", 500);
      resp.put("message", e.getMessage());
      resp.put("data", null);
      return resp;
    }
  }

  @PostMapping("/users/wechat-login")
  public Map<String, Object> wechatLogin(@RequestBody Map<String, Object> body) {
    Map<String, Object> resp = new HashMap<>();
    try {
      String code = (String) body.get("code");
      String phoneCode = (String) body.get("phoneCode");
      String encryptedData = (String) body.get("encryptedData");
      String iv = (String) body.get("iv");

      @SuppressWarnings("unchecked")
      Map<String, Object> userInfo = (Map<String, Object>) body.get("userInfo");

      if (code == null || code.isBlank()) {
        resp.put("code", 400);
        resp.put("message", "code is required");
        resp.put("data", null);
        return resp;
      }

      // 检查微信配置
      if (!weChatService.isConfigured()) {
        resp.put("code", 500);
        resp.put("message", "微信小程序配置未完成，请联系管理员配置AppID和AppSecret");
        resp.put("data", null);
        return resp;
      }

      // 调用微信官方API获取openid
      Map<String, Object> wechatResult = weChatService.getAccessToken(code);

      if (!(Boolean) wechatResult.get("success")) {
        resp.put("code", 400);
        resp.put("message", "微信登录失败: " + wechatResult.get("error"));
        resp.put("data", null);
        return resp;
      }

      String openid = (String) wechatResult.get("openid");
      String unionid = (String) wechatResult.get("unionid");
      String sessionKey = (String) wechatResult.get("session_key");

      // 尝试获取手机号
      String phoneNumber = null;
      if (phoneCode != null && !phoneCode.isBlank()) {
        // 使用phoneCode获取手机号
        Map<String, Object> phoneResult = weChatService.getPhoneNumberByCode(phoneCode);
        if ((Boolean) phoneResult.get("success")) {
          phoneNumber = (String) phoneResult.get("phoneNumber");
        }
      } else if (encryptedData != null && iv != null && sessionKey != null) {
        // 使用加密数据解密手机号
        phoneNumber = weChatService.getPhoneNumber(encryptedData, iv, sessionKey);
      }

      // 提取用户信息
      String nickname = (String) userInfo.getOrDefault("nickName", "微信用户");
      String avatarUrl = (String) userInfo.getOrDefault("avatarUrl", "");

      // 更新或创建用户
      User user = userService.createOrUpdateByOpenidAndPhone(openid, nickname, avatarUrl, phoneNumber, unionid);

      // 生成token
      String token = "token_" + UUID.randomUUID().toString().replace("-", "");

      resp.put("code", 0);
      resp.put("message", "ok");
      resp.put("data", Map.of(
        "user", convertToUserProfile(user),
        "token", token
      ));
      return resp;
    } catch (Exception e) {
      resp.put("code", 500);
      resp.put("message", "微信登录失败: " + e.getMessage());
      resp.put("data", null);
      return resp;
    }
  }

  @GetMapping("/users")
  public Map<String, Object> listUsers() {
    Map<String, Object> resp = new HashMap<>();
    try {
      // 这里可以添加分页逻辑
      List<User> users = List.of(); // 暂时返回空列表，实际需要实现分页查询

      Map<String, Object> data = new HashMap<>();
      data.put("list", users.stream().map(this::convertToUserProfile).collect(Collectors.toList()));
      data.put("total", users.size());

      resp.put("code", 0);
      resp.put("message", "ok");
      resp.put("data", data);
      return resp;
    } catch (Exception e) {
      resp.put("code", 500);
      resp.put("message", e.getMessage());
      resp.put("data", null);
      return resp;
    }
  }

  // 转换User实体为UserProfile格式，保持前端兼容性
  private Map<String, Object> convertToUserProfile(User user) {
    Map<String, Object> profile = new HashMap<>();
    profile.put("id", user.getUserId());
    profile.put("openid", user.getOpenid());
    profile.put("unionid", user.getUnionid());
    profile.put("nickname", user.getNickname());
    profile.put("avatarUrl", user.getAvatarUrl()); // 修改为前端期望的字段名
    profile.put("phone", user.getMobile());
    profile.put("lastLoginAt", user.getLastLoginAt() != null ?
        user.getLastLoginAt().toString().replace("T", " ").substring(0, 19) : null);
    return profile;
  }
}
