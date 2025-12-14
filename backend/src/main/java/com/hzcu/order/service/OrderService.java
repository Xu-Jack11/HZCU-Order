package com.hzcu.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.dto.CreateOrderRequest;
import com.hzcu.order.model.Order;
import com.hzcu.order.model.OrderGoods;
import com.hzcu.order.model.Shop;
import com.hzcu.order.repository.OrderJdbcRepository;

@Service
public class OrderService {

  private final DataStore dataStore;
  private final OrderJdbcRepository orderRepo;

  public OrderService(DataStore dataStore, OrderJdbcRepository orderRepo) {
    this.dataStore = dataStore;
    this.orderRepo = orderRepo;
  }

  public PageResult<Order> listOrders(String status, int page, int pageSize) {
    // Switch to DB repository
    List<Order> paged = orderRepo.findAll(status, page, pageSize);
    int total = orderRepo.count(status);
    return new PageResult<>(paged, total);
  }

  public Order createOrder(CreateOrderRequest request) {
    Shop shop = dataStore.findShop(request.getShopId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "canteen not found"));

    List<OrderGoods> goods = request.getCartList().stream()
        .map(item -> new OrderGoods(
            item.getId(),
            item.getName(),
            item.getImage(),
            item.getPrice(),
            item.getCount()
        ))
        .collect(Collectors.toList());
    int totalCount = goods.stream().mapToInt(OrderGoods::getCount).sum();

    Order order = new Order();
    order.setShopId(shop.getId());
    order.setShopName(shop.getName());
    order.setShopLogo(shop.getLogo());
    order.setGoods(goods);
    order.setTotalCount(totalCount);
    order.setTotalPrice(request.getTotalPrice());
    order.setStatus("pending");
    order.setStatusText(resolveStatusText("pending"));
    order.setCreateTime(LocalDateTime.now().format(dataStore.getFormatter()));
    order.setDiningMode(request.getDiningMode());
    order.setTableNo(request.getTableNo());
    order.setPickupTime(request.getPickupTime());
    order.setRemark(request.getRemark());

    // Save to DB instead of memory
    return orderRepo.create(order);
  }

  public Order cancel(long orderId) {
    Order order = orderRepo.findById(orderId);
    if (order == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found");
    }
    if ("completed".equalsIgnoreCase(order.getStatus()) || "canceled".equalsIgnoreCase(order.getStatus())) {
      return order;
    }
    String nextStatus = "canceled";
    String nextText = resolveStatusText(nextStatus);
    orderRepo.updateStatus(orderId, nextStatus, nextText);
    order.setStatus(nextStatus);
    order.setStatusText(nextText);
    return order;
  }

  public Order pay(long orderId) {
    Order order = orderRepo.findById(orderId);
    if (order == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found");
    }
    if ("pending".equalsIgnoreCase(order.getStatus())) {
      String nextStatus = "preparing";
      String nextText = resolveStatusText(nextStatus);
      orderRepo.updateStatus(orderId, nextStatus, nextText);
      order.setStatus(nextStatus);
      order.setStatusText(nextText);
    }
    return order;
  }

  public Order complete(long orderId) {
    Order order = orderRepo.findById(orderId);
    if (order == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found");
    }
    String nextStatus = "completed";
    String nextText = resolveStatusText(nextStatus);
    orderRepo.updateStatus(orderId, nextStatus, nextText);
    order.setStatus(nextStatus);
    order.setStatusText(nextText);
    return order;
  }

  // Deprecated: switched to JDBC repository for persistence
  // private Order findOrder(long orderId) { ... }

  private String resolveStatusText(String status) {
    if (!StringUtils.hasText(status)) {
      return "";
    }
    switch (status.toLowerCase(Locale.ROOT)) {
      case "pending":
        return "待付款";
      case "preparing":
        return "制作中";
      case "ready":
        return "待取餐";
      case "completed":
        return "已完成";
      case "canceled":
        return "已取消";
      default:
        return status;
    }
  }
}
