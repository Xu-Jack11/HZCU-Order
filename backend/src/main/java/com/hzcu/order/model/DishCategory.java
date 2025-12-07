package com.hzcu.order.model;

import java.util.List;

public class DishCategory {

  private long id;
  private String name;
  private List<Dish> goods;

  public DishCategory() {
  }

  public DishCategory(long id, String name, List<Dish> goods) {
    this.id = id;
    this.name = name;
    this.goods = goods;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Dish> getGoods() {
    return goods;
  }

  public void setGoods(List<Dish> goods) {
    this.goods = goods;
  }
}
