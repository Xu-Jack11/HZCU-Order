package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.common.PageResult;
import com.hzcu.order.model.Comment;
import com.hzcu.order.model.DishCategory;
import com.hzcu.order.model.HomeFeed;
import com.hzcu.order.model.Shop;
import com.hzcu.order.service.CanteenService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CanteenController {

  private final CanteenService canteenService;

  public CanteenController(CanteenService canteenService) {
    this.canteenService = canteenService;
  }

  @GetMapping("/home/feed")
  public ApiResponse<HomeFeed> homeFeed() {
    return ApiResponse.success(canteenService.getHomeFeed());
  }

  @GetMapping("/canteens")
  public ApiResponse<PageResult<Shop>> listCanteens(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int pageSize,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) String sort) {
    int currentPage = Math.max(page, 1);
    int size = Math.max(pageSize, 1);
    return ApiResponse.success(canteenService.queryCanteens(currentPage, size, keyword, categoryId, sort));
  }

  @GetMapping("/canteens/{id}")
  public ApiResponse<Shop> getCanteen(@PathVariable("id") long id) {
    return ApiResponse.success(canteenService.getShop(id));
  }

  @GetMapping("/canteens/{id}/dishes")
  public ApiResponse<List<DishCategory>> getCanteenDishes(@PathVariable("id") long id) {
    return ApiResponse.success(canteenService.getDishCategories(id));
  }

  @GetMapping("/shops/{id}/comments")
  public ApiResponse<List<Comment>> getComments(@PathVariable("id") long id) {
    return ApiResponse.success(canteenService.getComments(id));
  }
}
