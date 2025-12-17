package com.hzcu.order.repository;

import com.hzcu.order.model.Dish;
import com.hzcu.order.model.DishCategory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DishJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public DishJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<DishCategory> findCategoriesByShopId(long shopId) {
    String sql = "SELECT id, name FROM dish_categories WHERE shop_id = ? ORDER BY sort_order ASC";
    return jdbcTemplate.query(sql, new RowMapper<DishCategory>() {
      @Override
      public DishCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        DishCategory category = new DishCategory();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        // Dishes will be populated separately
        return category;
      }
    }, shopId);
  }

  public List<Dish> findDishesByCategoryId(long categoryId) {
    String sql = "SELECT id, name, description, image, price, original_price, monthly_sales, good_rate, tags FROM dishes WHERE category_id = ?";
    return jdbcTemplate.query(sql, new RowMapper<Dish>() {
      @Override
      public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dish dish = new Dish();
        dish.setId(rs.getLong("id"));
        dish.setName(rs.getString("name"));
        dish.setDescription(rs.getString("description"));
        dish.setImage(rs.getString("image"));
        dish.setPrice(rs.getDouble("price"));
        dish.setOriginalPrice(rs.getObject("original_price") != null ? rs.getDouble("original_price") : null);
        dish.setMonthlySales(rs.getInt("monthly_sales"));
        dish.setGoodRate(rs.getInt("good_rate"));
        
        String tagsStr = rs.getString("tags");
        if (tagsStr != null && !tagsStr.isEmpty()) {
            // Simple comma separation for now
            dish.setTags(Arrays.asList(tagsStr.split(","))); 
        } else {
            dish.setTags(Collections.emptyList());
        }
        
        dish.setCount(0); // Default count in cart
        return dish;
      }
    }, categoryId);
  }

  public void updateDish(long dishId, java.util.Map<String, Object> payload) {
    // Build dynamic SQL based on provided fields: name, description, image, price, category_id
    StringBuilder sql = new StringBuilder("UPDATE dishes SET ");
    java.util.List<Object> params = new java.util.ArrayList<>();
    boolean first = true;
    if (payload.containsKey("name")) { sql.append(first?"":" ,").append("name = ?"); params.add(String.valueOf(payload.get("name"))); first=false; }
    if (payload.containsKey("description")) { sql.append(first?"":" ,").append("description = ?"); params.add(String.valueOf(payload.get("description"))); first=false; }
    if (payload.containsKey("image")) { sql.append(first?"":" ,").append("image = ?"); params.add(String.valueOf(payload.get("image"))); first=false; }
    if (payload.containsKey("price")) { sql.append(first?"":" ,").append("price = ?"); params.add(Double.valueOf(String.valueOf(payload.get("price")))); first=false; }
    if (payload.containsKey("categoryId")) { sql.append(first?"":" ,").append("category_id = ?"); params.add(Long.valueOf(String.valueOf(payload.get("categoryId")))); first=false; }
    if (first) { return; } // nothing to update
    sql.append(" WHERE id = ?");
    params.add(dishId);
    jdbcTemplate.update(sql.toString(), params.toArray());
  }

  public void updateAvailability(long dishId, boolean isAvailable) {
    String sql = "UPDATE dishes SET is_available = ? WHERE id = ?";
    jdbcTemplate.update(sql, isAvailable ? 1 : 0, dishId);
  }
}
