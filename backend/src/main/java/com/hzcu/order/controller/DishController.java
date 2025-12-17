package com.hzcu.order.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.service.DishService;

@RestController
@CrossOrigin
public class DishController {

  private final DishService dishService;

  public DishController(DishService dishService) {
    this.dishService = dishService;
  }

  @PutMapping("/merchant/dishes/{id}")
  public ApiResponse<Boolean> updateDish(
      @PathVariable("id") long id,
      @RequestBody Map<String, Object> payload
  ) {
    dishService.updateDish(id, payload);
    return ApiResponse.success(true);
  }

  @PostMapping("/merchant/dishes/{id}/availability")
  public ApiResponse<Boolean> updateAvailability(
      @PathVariable("id") long id,
      @RequestParam("isAvailable") boolean isAvailable
  ) {
    dishService.updateAvailability(id, isAvailable);
    return ApiResponse.success(true);
  }
}
