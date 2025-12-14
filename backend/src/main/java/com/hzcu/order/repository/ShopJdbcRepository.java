package com.hzcu.order.repository;

import com.hzcu.order.model.Shop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@ConditionalOnClass(JdbcTemplate.class)
@Repository
public class ShopJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public ShopJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Shop> findAll() {
    // 仅选择各库都较为通用的字段；其余在映射时给默认值
    String sql = "SELECT id, name, logo, rating FROM shops ORDER BY id ASC";
    return jdbcTemplate.query(sql, new RowMapper<Shop>() {
      @Override
      public Shop mapRow(ResultSet rs, int rowNum) throws SQLException {
        Shop s = new Shop();
        s.setId(rs.getLong("id"));
        s.setName(rs.getString("name"));
        s.setLogo(rs.getString("logo"));
        try {
          s.setRating(rs.getDouble("rating"));
        } catch (SQLException e) {
          s.setRating(4.5);
        }
        s.setMonthlySales(0);
        s.setWaitTime(10);
        s.setDistance("1.7km");
        s.setMinPrice(0);
        s.setTags(java.util.List.of());
        s.setCategoryIds(java.util.List.of());
        s.setNotice("");
        s.setAddress("");
        s.setBusinessHours("");
        s.setPhone("");
        return s;
      }
    });
  }

  public Shop findById(long id) {
    String sql = "SELECT id, name, logo, rating FROM shops WHERE id = ?";
    List<Shop> list = jdbcTemplate.query(sql, new RowMapper<Shop>() {
      @Override
      public Shop mapRow(ResultSet rs, int rowNum) throws SQLException {
        Shop s = new Shop();
        s.setId(rs.getLong("id"));
        s.setName(rs.getString("name"));
        s.setLogo(rs.getString("logo"));
        try {
          s.setRating(rs.getDouble("rating"));
        } catch (SQLException e) {
          s.setRating(4.5);
        }
        s.setMonthlySales(0);
        s.setWaitTime(10);
        s.setDistance("1.7km");
        s.setMinPrice(0);
        s.setTags(java.util.List.of());
        s.setCategoryIds(java.util.List.of());
        s.setNotice("");
        s.setAddress("");
        s.setBusinessHours("");
        s.setPhone("");
        return s;
      }
    }, id);
    return list.isEmpty() ? null : list.get(0);
  }

  public Shop create(String name, String logo, double rating) {
    String sql = "INSERT INTO shops (name, logo, rating) VALUES (?, ?, ?)";
    org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
    jdbcTemplate.update((Connection con) -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, name);
      ps.setString(2, logo);
      ps.setDouble(3, rating);
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    long id = key != null ? key.longValue() : 0L;
    Shop s = new Shop();
    s.setId(id);
    s.setName(name);
    s.setLogo(logo);
    s.setRating(rating);
    s.setMonthlySales(0);
    s.setWaitTime(10);
    s.setDistance("1.7km");
    s.setMinPrice(0);
    s.setTags(java.util.List.of());
    s.setCategoryIds(java.util.List.of());
    s.setNotice("");
    s.setAddress("");
    s.setBusinessHours("");
    s.setPhone("");
    return s;
  }
}
