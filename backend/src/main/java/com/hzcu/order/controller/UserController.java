package com.hzcu.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.data.DataStore;
import com.hzcu.order.model.UserProfile;

@RestController
public class UserController {

  private final DataStore dataStore;

  public UserController(DataStore dataStore) {
    this.dataStore = dataStore;
  }

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
      UserProfile user = dataStore.upsertUserByPhone(phone, nickname != null ? nickname : phone, avatar);
      resp.put("code", 0);
      resp.put("message", "ok");
      resp.put("data", user);
      return resp;
    } catch (Exception e) {
      resp.put("code", 500);
      resp.put("message", e.getMessage());
      resp.put("data", null);
      return resp;
    }
  }

  @GetMapping("/users")
  public Map<String, Object> listUsers() {
    Map<String, Object> resp = new HashMap<>();
    try {
      List<UserProfile> list = dataStore.getUsers();
      Map<String, Object> data = new HashMap<>();
      data.put("list", list);
      data.put("total", list.size());
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
}
