package com.hzcu.order.repository;

import com.hzcu.order.model.Order;
import com.hzcu.order.model.OrderGoods;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public OrderJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Order create(Order order) {
    String sql = "INSERT INTO orders (shop_id, shop_name, shop_logo, total_count, total_price, status, status_text, create_time, dining_mode, table_no, pickup_time, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    
    jdbcTemplate.update((Connection con) -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, order.getShopId());
      ps.setString(2, order.getShopName());
      ps.setString(3, order.getShopLogo());
      ps.setInt(4, order.getTotalCount());
      ps.setDouble(5, order.getTotalPrice());
      ps.setString(6, order.getStatus());
      ps.setString(7, order.getStatusText());
      ps.setString(8, order.getCreateTime());
      ps.setString(9, order.getDiningMode());
      ps.setString(10, order.getTableNo());
      ps.setString(11, order.getPickupTime());
      ps.setString(12, order.getRemark());
      return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key != null) {
        order.setId(key.longValue());
        saveOrderGoods(order.getId(), order.getGoods());
    }
    return order;
  }

  private void saveOrderGoods(long orderId, List<OrderGoods> goods) {
      if (goods == null || goods.isEmpty()) return;
      String sql = "INSERT INTO order_goods (order_id, goods_id, name, image, price, count) VALUES (?, ?, ?, ?, ?, ?)";
      for (OrderGoods item : goods) {
          jdbcTemplate.update(sql, orderId, item.getId(), item.getName(), item.getImage(), item.getPrice(), item.getCount());
      }
  }

  public List<Order> findAll(String status, int page, int pageSize) {
    // Basic implementation: if table doesn't exist or is empty, returns empty list.
    // We will create the table shortly.
    StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
    if (status != null && !"all".equalsIgnoreCase(status)) {
        sql.append(" AND status = ?");
    }
    sql.append(" ORDER BY create_time DESC LIMIT ? OFFSET ?");

    Object[] args;
    int offset = (page - 1) * pageSize;
    if (status != null && !"all".equalsIgnoreCase(status)) {
        args = new Object[]{status, pageSize, offset};
    } else {
        args = new Object[]{pageSize, offset};
    }

    try {
        return jdbcTemplate.query(sql.toString(), new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setShopId(rs.getLong("shop_id"));
                order.setShopName(rs.getString("shop_name"));
                order.setShopLogo(rs.getString("shop_logo"));
                order.setTotalCount(rs.getInt("total_count"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setStatusText(rs.getString("status_text"));
                order.setCreateTime(rs.getString("create_time"));
                order.setDiningMode(rs.getString("dining_mode"));
                order.setTableNo(rs.getString("table_no"));
                order.setPickupTime(rs.getString("pickup_time"));
                order.setRemark(rs.getString("remark"));
                
                // Fetch goods for this order
                order.setGoods(findGoodsByOrderId(order.getId()));
                return order;
            }
        }, args);
    } catch (Exception e) {
        // If table doesn't exist, return empty list
        return Collections.emptyList();
    }
  }

  private List<OrderGoods> findGoodsByOrderId(long orderId) {
      String sql = "SELECT goods_id, name, image, price, count FROM order_goods WHERE order_id = ?";
      try {
          return jdbcTemplate.query(sql, (rs, rowNum) -> {
              return new OrderGoods(
                  rs.getLong("goods_id"),
                  rs.getString("name"),
                  rs.getString("image"),
                  rs.getDouble("price"),
                  rs.getInt("count")
              );
          }, orderId);
      } catch (Exception e) {
          return Collections.emptyList();
      }
  }
  
  public int count(String status) {
      String sql = "SELECT count(*) FROM orders";
      if (status != null && !"all".equalsIgnoreCase(status)) {
          sql += " WHERE status = ?";
          try {
            return jdbcTemplate.queryForObject(sql, Integer.class, status);
          } catch (Exception e) { return 0; }
      }
      try {
        return jdbcTemplate.queryForObject(sql, Integer.class);
      } catch (Exception e) { return 0; }
  }

  public Order findById(long orderId) {
      String sql = "SELECT * FROM orders WHERE id = ?";
      try {
          return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
              Order order = new Order();
              order.setId(rs.getLong("id"));
              order.setShopId(rs.getLong("shop_id"));
              order.setShopName(rs.getString("shop_name"));
              order.setShopLogo(rs.getString("shop_logo"));
              order.setTotalCount(rs.getInt("total_count"));
              order.setTotalPrice(rs.getDouble("total_price"));
              order.setStatus(rs.getString("status"));
              order.setStatusText(rs.getString("status_text"));
              order.setCreateTime(rs.getString("create_time"));
              order.setDiningMode(rs.getString("dining_mode"));
              order.setTableNo(rs.getString("table_no"));
              order.setPickupTime(rs.getString("pickup_time"));
              order.setRemark(rs.getString("remark"));
              order.setGoods(findGoodsByOrderId(order.getId()));
              return order;
          }, orderId);
      } catch (Exception e) {
          return null;
      }
  }

  public void updateStatus(long orderId, String status, String statusText) {
      String sql = "UPDATE orders SET status = ?, status_text = ? WHERE id = ?";
      jdbcTemplate.update(sql, status, statusText, orderId);
  }
}
