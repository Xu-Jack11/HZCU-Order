package com.hzcu.order.model;

import java.util.List;

public class Dish {

  private long id;
  private String name;
  private String description;
  private String image;
  private double price;
  private Double originalPrice;
  private int monthlySales;
  private int goodRate;
  private List<String> tags;
  private int count;

  public Dish() {
  }

  public Dish(long id, String name, String description, String image, double price, Double originalPrice,
      int monthlySales, int goodRate, List<String> tags, int count) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.originalPrice = originalPrice;
    this.monthlySales = monthlySales;
    this.goodRate = goodRate;
    this.tags = tags;
    this.count = count;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Double getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(Double originalPrice) {
    this.originalPrice = originalPrice;
  }

  public int getMonthlySales() {
    return monthlySales;
  }

  public void setMonthlySales(int monthlySales) {
    this.monthlySales = monthlySales;
  }

  public int getGoodRate() {
    return goodRate;
  }

  public void setGoodRate(int goodRate) {
    this.goodRate = goodRate;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
