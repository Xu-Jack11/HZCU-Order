package com.hzcu.order.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.dto.ShopCreateRequest;
import com.hzcu.order.model.Comment;
import com.hzcu.order.model.DishCategory;
import com.hzcu.order.model.HomeFeed;
import com.hzcu.order.model.Shop;
import com.hzcu.order.service.CanteenService;

@RestController
@CrossOrigin
public class CanteenController {

  private final CanteenService canteenService;
  private final DataStore dataStore;
  private static final Logger log = LoggerFactory.getLogger(CanteenController.class);

  public CanteenController(CanteenService canteenService, DataStore dataStore) {
    this.canteenService = canteenService;
    this.dataStore = dataStore;
  }

  @GetMapping("/home/feed")
  public ApiResponse<HomeFeed> homeFeed() {
    return ApiResponse.success(canteenService.getHomeFeed());
  }

  @PostMapping("/canteens")
  public ApiResponse<Shop> createCanteen(@Validated @org.springframework.web.bind.annotation.RequestBody ShopCreateRequest req) {
    Shop created = canteenService.createShop(req.getName(), req.getLogo(), req.getRating());
    return ApiResponse.success(created);
  }

  @GetMapping("/canteens")
    public ApiResponse<PageResult<Shop>> listCanteens(
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "categoryId", required = false) Integer categoryId,
      @RequestParam(name = "sort", required = false) String sort) {
    try {
      int currentPage = Math.max(page, 1);
      int size = Math.max(pageSize, 1);
      PageResult<Shop> result = canteenService.queryCanteens(currentPage, size, keyword, categoryId, sort);
      return ApiResponse.success(result);
    } catch (Exception e) {
      // 兜底：记录异常并返回全部示例数据，避免前端空白
      log.error("/canteens error", e);
      List<Shop> all = dataStore.getShops();
      PageResult<Shop> fallback = new PageResult<>(all, all.size());
      return ApiResponse.success(fallback);
    }
  }

  @GetMapping("/health")
  public ApiResponse<String> health() {
    return ApiResponse.success("ok");
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
