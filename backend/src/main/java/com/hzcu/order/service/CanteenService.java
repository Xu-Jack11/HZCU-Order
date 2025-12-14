package com.hzcu.order.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.model.Comment;
import com.hzcu.order.model.DishCategory;
import com.hzcu.order.model.HomeFeed;
import com.hzcu.order.model.Shop;
import com.hzcu.order.repository.DishJdbcRepository;
import com.hzcu.order.repository.ShopJdbcRepository;

@Service
public class CanteenService {

  private final DataStore dataStore;
  private final ObjectProvider<ShopJdbcRepository> shopRepoProvider;
  private final DishJdbcRepository dishRepo;

  public CanteenService(DataStore dataStore, ObjectProvider<ShopJdbcRepository> shopRepoProvider, DishJdbcRepository dishRepo) {
    this.dataStore = dataStore;
    this.shopRepoProvider = shopRepoProvider;
    this.dishRepo = dishRepo;
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
    List<Shop> source = getShopSource();
    List<Shop> filtered = source.stream()
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

  // 仅从数据库读取，不再回退到内存数据
  private List<Shop> getShopSource() {
    try {
      ShopJdbcRepository shopRepo = shopRepoProvider.getIfAvailable();
      if (shopRepo != null) {
        return shopRepo.findAll();
      }
    } catch (Exception e) {
      e.printStackTrace(); // 打印错误日志以便排查
    }
    // 数据库不可用或查询失败，返回空列表，不显示假数据
    return List.of();
  }

  public Shop getShop(long shopId) {
    ShopJdbcRepository repo = shopRepoProvider.getIfAvailable();
    if (repo != null) {
      try {
        Shop dbShop = repo.findById(shopId);
        if (dbShop != null) {
          return dbShop;
        }
      } catch (Exception ignore) {
      }
    }
    // 数据库查不到直接抛出 404，不再查内存
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "canteen not found");
  }

  public List<DishCategory> getDishCategories(long shopId) {
    ensureShopExists(shopId);
    
    // Try DB first
    try {
        List<DishCategory> categories = dishRepo.findCategoriesByShopId(shopId);
        if (categories != null && !categories.isEmpty()) {
            for (DishCategory cat : categories) {
                cat.setGoods(dishRepo.findDishesByCategoryId(cat.getId()));
            }
            return categories;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return dataStore.getDishCategories(shopId);
  }

  public List<Comment> getComments(long shopId) {
    ensureShopExists(shopId);
    return dataStore.getComments(shopId);
  }

  public Shop createShop(String name, String logo, double rating) {
    ShopJdbcRepository repo = shopRepoProvider.getIfAvailable();
    if (repo == null) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database repository not available");
    }
    try {
      return repo.create(name, logo, rating);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "create shop failed");
    }
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
    // Reuse getShop logic which checks DB
    getShop(shopId);
  }
}
