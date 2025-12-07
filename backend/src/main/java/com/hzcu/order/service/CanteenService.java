package com.hzcu.order.service;

import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.model.Comment;
import com.hzcu.order.model.DishCategory;
import com.hzcu.order.model.HomeFeed;
import com.hzcu.order.model.Shop;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class CanteenService {

  private final DataStore dataStore;

  public CanteenService(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  public HomeFeed getHomeFeed() {
    List<Shop> recommend = dataStore.getShops().stream().limit(3).collect(Collectors.toList());
    return new HomeFeed(
        List.of("/images/banner/banner1.png", "/images/banner/banner2.png"),
        List.of("欢迎使用城院点餐小程序", "下单前请确认取餐时间"),
        recommend
    );
  }

  public PageResult<Shop> queryCanteens(int page, int pageSize, String keyword, Integer categoryId, String sort) {
    List<Shop> filtered = dataStore.getShops().stream()
        .filter(shop -> categoryId == null || categoryId == 0 || shop.getCategoryIds().contains(categoryId))
        .filter(shop -> {
          if (!StringUtils.hasText(keyword)) {
            return true;
          }
          String lower = keyword.toLowerCase(Locale.ROOT);
          boolean nameMatch = shop.getName().toLowerCase(Locale.ROOT).contains(lower);
          boolean tagMatch = shop.getTags().stream().anyMatch(tag -> tag.toLowerCase(Locale.ROOT).contains(lower));
          return nameMatch || tagMatch;
        })
        .sorted(getComparator(sort))
        .collect(Collectors.toList());

    int fromIndex = Math.max((page - 1) * pageSize, 0);
    int toIndex = Math.min(fromIndex + pageSize, filtered.size());
    List<Shop> paged = fromIndex >= filtered.size() ? List.of() : filtered.subList(fromIndex, toIndex);
    return new PageResult<>(paged, filtered.size());
  }

  public Shop getShop(long shopId) {
    return dataStore.findShop(shopId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "canteen not found"));
  }

  public List<DishCategory> getDishCategories(long shopId) {
    ensureShopExists(shopId);
    return dataStore.getDishCategories(shopId);
  }

  public List<Comment> getComments(long shopId) {
    ensureShopExists(shopId);
    return dataStore.getComments(shopId);
  }

  private Comparator<Shop> getComparator(String sort) {
    if (!StringUtils.hasText(sort)) {
      return Comparator.comparingLong(Shop::getId);
    }
    switch (sort) {
      case "nearby":
        return Comparator.comparingDouble(this::parseDistance);
      case "discover":
        return Comparator.comparingDouble(Shop::getRating).reversed()
            .thenComparing(Comparator.comparingInt(Shop::getMonthlySales).reversed());
      case "hot":
        return Comparator.comparingInt(Shop::getMonthlySales).reversed();
      default:
        return Comparator.comparingLong(Shop::getId);
    }
  }

  private double parseDistance(Shop shop) {
    String distance = shop.getDistance();
    if (!StringUtils.hasText(distance)) {
      return Double.MAX_VALUE;
    }
    String normalized = distance.replace("km", "").trim();
    try {
      return Double.parseDouble(normalized);
    } catch (NumberFormatException e) {
      return Double.MAX_VALUE;
    }
  }

  private void ensureShopExists(long shopId) {
    if (dataStore.findShop(shopId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "canteen not found");
    }
  }
}
