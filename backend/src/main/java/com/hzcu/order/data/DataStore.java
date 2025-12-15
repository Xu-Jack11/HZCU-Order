package com.hzcu.order.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.hzcu.order.model.Comment;
import com.hzcu.order.model.Dish;
import com.hzcu.order.model.DishCategory;
import com.hzcu.order.model.Order;
import com.hzcu.order.model.OrderGoods;
import com.hzcu.order.model.Shop;
import com.hzcu.order.model.UserProfile;

import jakarta.annotation.PostConstruct;

@Component
public class DataStore {

  private final List<Shop> shops = new ArrayList<>();
  private final Map<Long, List<DishCategory>> dishCategories = new HashMap<>();
  private final Map<Long, List<Comment>> shopComments = new HashMap<>();
  private final List<Order> orders = new ArrayList<>();
  private final List<UserProfile> users = new ArrayList<>();
  private final AtomicLong orderIdGenerator = new AtomicLong(1000);
  private final AtomicLong userIdGenerator = new AtomicLong(1);
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @PostConstruct
  public void init() {
    seedShops();
    seedDishes();
    seedComments();
    seedOrders();
  }

  public List<Shop> getShops() {
    return shops;
  }

  public Optional<Shop> findShop(long shopId) {
    return shops.stream().filter(s -> s.getId() == shopId).findFirst();
  }

  public List<DishCategory> getDishCategories(long shopId) {
    return dishCategories.getOrDefault(shopId, List.of());
  }

  public List<Comment> getComments(long shopId) {
    return shopComments.getOrDefault(shopId, List.of());
  }

  public List<Order> getOrders() {
    return orders;
  }

  public List<UserProfile> getUsers() {
    return users;
  }

  public synchronized UserProfile upsertUserByPhone(String phone, String nickname, String avatar) {
    return users.stream().filter(u -> phone != null && phone.equals(u.getPhone())).findFirst()
        .map(u -> {
          if (nickname != null) u.setNickname(nickname);
          if (avatar != null) u.setAvatar(avatar);
          return u;
        })
        .orElseGet(() -> {
          UserProfile u = new UserProfile(userIdGenerator.getAndIncrement(), nickname != null ? nickname : phone, avatar, phone);
          users.add(u);
          return u;
        });
  }

  public Order addOrder(Order order) {
    long id = orderIdGenerator.incrementAndGet();
    order.setId(id);
    if (order.getCreateTime() == null) {
      order.setCreateTime(LocalDateTime.now().format(formatter));
    }
    orders.add(order);
    return order;
  }

  public DateTimeFormatter getFormatter() {
    return formatter;
  }

  private void seedShops() {
    shops.add(new Shop(
        1L,
        "肯德基（城院店）",
        "/images/shops/kfc.png",
        4.8,
        719,
        15,
        "1.8km",
        0,
        List.of("20减8", "30减12"),
        List.of(1, 2, 4),
        "共赏元月一轮，喜迎中秋良宵。",
        "浙江省杭州市拱墅区湖州街51号",
        "09:00-22:00",
        "0571-88888888"
    ));
    shops.add(new Shop(
        2L,
        "兰州拉面",
        "/images/shops/lamian.png",
        4.7,
        1750,
        10,
        "2.4km",
        0,
        List.of("45减30", "75减45"),
        List.of(5, 6),
        "今日牛肉分量加倍，欢迎堂食。",
        "浙大城院南校区学而路15号",
        "10:30-21:30",
        "0571-88998899"
    ));
    shops.add(new Shop(
        3L,
        "库迪咖啡（城院南校区店）",
        "/images/shops/cotti.png",
        4.8,
        1613,
        12,
        "1.7km",
        0,
        List.of("20减12", "35减19"),
        List.of(3, 4),
        "新品豆子上架，下午场限时买一赠一。",
        "浙江省杭州市湖州街51号-1",
        "08:00-22:00",
        "0571-88776655"
    ));
  }

  private void seedDishes() {
    List<DishCategory> kfcCategories = new ArrayList<>();
    kfcCategories.add(new DishCategory(
        1L,
        "热销",
        List.of(
            new Dish(101, "嫩牛五方超值单人餐", "热卖品类优质商品", "/images/goods/niuwufang.png", 19.5, 19.7, 283, 97,
                List.of("9.9折"), 0),
            new Dish(102, "香辣鸡腿堡单人餐", "经典人气套餐", "/images/goods/jileitui.png", 25.9, 28.0, 456, 95,
                List.of("热销"), 0)
        )
    ));
    kfcCategories.add(new DishCategory(
        2L,
        "优惠",
        List.of(
            new Dish(201, "超值午餐套餐", "限时特惠", "/images/goods/lunch.png", 15.9, 22.0, 189, 94, List.of("限时"),
                0)
        )
    ));
    kfcCategories.add(new DishCategory(
        3L,
        "单人套餐",
        List.of(
            new Dish(301, "奥尔良烤鸡腿堡套餐", "含中薯+中可乐", "/images/goods/orleans.png", 32.0, null, 312, 96,
                List.of(), 0)
        )
    ));
    kfcCategories.add(new DishCategory(
        4L,
        "套餐",
        List.of(
            new Dish(401, "双人欢享套餐", "2个汉堡+2份薯条+2杯可乐", "/images/goods/double.png", 59.0, 68.0, 98, 98,
                List.of("人气"), 0)
        )
    ));

    List<DishCategory> noodleCategories = new ArrayList<>();
    noodleCategories.add(new DishCategory(
        5L,
        "面食",
        List.of(
            new Dish(501, "兰州牛肉拉面", "经典清汤牛肉面", "/images/goods/lamian.png", 15.0, null, 1200, 96,
                List.of("招牌"), 0),
            new Dish(502, "番茄鸡蛋面", "家常口味", "/images/goods/noodle.png", 12.0, null, 650, 94,
                List.of("热销"), 0)
        )
    ));
    noodleCategories.add(new DishCategory(
        6L,
        "套餐",
        List.of(
          new Dish(503, "牛肉面+小菜套餐", "含凉拌木耳", "/images/goods/set.png", 22.0, 26.0, 480, 95,
              List.of("优惠"), 0)
        )
    ));

    List<DishCategory> coffeeCategories = new ArrayList<>();
    coffeeCategories.add(new DishCategory(
        7L,
        "咖啡",
        List.of(
            new Dish(601, "美式咖啡", "经典美式", "/images/goods/coffee.png", 9.9, null, 980, 97, List.of("热销"), 0),
            new Dish(602, "拿铁咖啡", "丝滑牛奶", "/images/goods/latte.png", 14.0, null, 860, 96,
                List.of("推荐"), 0)
        )
    ));
    coffeeCategories.add(new DishCategory(
        8L,
        "小食",
        List.of(
            new Dish(603, "蓝莓芝士蛋糕", "下午茶优选", "/images/goods/cake.png", 16.0, 18.0, 320, 95,
                List.of("新品"), 0)
        )
    ));

    dishCategories.put(1L, kfcCategories);
    dishCategories.put(2L, noodleCategories);
    dishCategories.put(3L, coffeeCategories);
  }

  private void seedComments() {
    List<Comment> defaults = List.of(
        new Comment(1, "/images/avatar/user1.png", "美食家小王", "2024-01-15", 5, "味道很好，出餐也很快，推荐！"),
        new Comment(2, "/images/avatar/user2.png", "吃货达人", "2024-01-14", 4, "分量足，性价比高")
    );
    shopComments.put(1L, defaults);
    shopComments.put(2L, defaults);
    shopComments.put(3L, defaults);
  }

  private void seedOrders() {
    List<OrderGoods> firstGoods = List.of(
        new OrderGoods(101, "嫩牛五方超值单人餐", "/images/goods/niuwufang.png", 19.5, 1),
        new OrderGoods(102, "香辣鸡腿堡单人餐", "/images/goods/jileitui.png", 25.9, 1)
    );
    orders.add(new Order(
        1L,
        1L,
        "肯德基（城院店）",
        "/images/shops/kfc.png",
        firstGoods,
        2,
        45.4,
        "completed",
        "已完成",
        "2024-01-15 12:30:00",
        "dine-in",
        null,
        null,
        null
    ));

    List<OrderGoods> secondGoods = List.of(
        new OrderGoods(501, "兰州牛肉拉面", "/images/goods/lamian.png", 15.0, 2)
    );
    orders.add(new Order(
        2L,
        2L,
        "兰州拉面",
        "/images/shops/lamian.png",
        secondGoods,
        2,
        30.0,
        "preparing",
        "制作中",
        "2024-01-15 11:00:00",
        "dine-in",
        null,
        null,
        null
    ));

    List<OrderGoods> thirdGoods = List.of(
        new OrderGoods(601, "美式咖啡", "/images/goods/coffee.png", 9.9, 1)
    );
    orders.add(new Order(
        3L,
        3L,
        "库迪咖啡（城院南校区店）",
        "/images/shops/cotti.png",
        thirdGoods,
        1,
        9.9,
        "pending",
        "待付款",
        "2024-01-15 10:30:00",
        "takeaway",
        null,
        null,
        null
    ));

    orderIdGenerator.set(orders.stream().mapToLong(Order::getId).max().orElse(1000));
  }
}
