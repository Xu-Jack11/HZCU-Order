package com.hzcu.order.service;

import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.dto.CreateOrderRequest;
import com.hzcu.order.model.Order;
import com.hzcu.order.model.OrderGoods;
import com.hzcu.order.model.Shop;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

  private final DataStore dataStore;

  public OrderService(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  public PageResult<Order> listOrders(String status, int page, int pageSize) {
    List<Order> filtered = dataStore.getOrders().stream()
        .filter(order -> !StringUtils.hasText(status) || "all".equalsIgnoreCase(status)
            || order.getStatus().equalsIgnoreCase(status))
        .sorted(Comparator.comparing(Order::getCreateTime).reversed())
        .collect(Collectors.toList());

    int fromIndex = Math.max((page - 1) * pageSize, 0);
    int toIndex = Math.min(fromIndex + pageSize, filtered.size());
    List<Order> paged = fromIndex >= filtered.size() ? List.of() : filtered.subList(fromIndex, toIndex);
    return new PageResult<>(paged, filtered.size());
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

    return dataStore.addOrder(order);
  }

  public Order cancel(long orderId) {
    Order order = findOrder(orderId);
    if ("completed".equalsIgnoreCase(order.getStatus()) || "canceled".equalsIgnoreCase(order.getStatus())) {
      return order;
    }
    order.setStatus("canceled");
    order.setStatusText(resolveStatusText("canceled"));
    return order;
  }

  public Order pay(long orderId) {
    Order order = findOrder(orderId);
    if ("pending".equalsIgnoreCase(order.getStatus())) {
      order.setStatus("preparing");
      order.setStatusText(resolveStatusText("preparing"));
    }
    return order;
  }

  public Order complete(long orderId) {
    Order order = findOrder(orderId);
    order.setStatus("completed");
    order.setStatusText(resolveStatusText("completed"));
    return order;
  }

  private Order findOrder(long orderId) {
    return dataStore.getOrders().stream()
        .filter(o -> o.getId() == orderId)
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
  }

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
