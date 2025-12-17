package com.hzcu.order.model;

public class OrderGoods {

  private long id;
  private String name;
  private String image;
  private double price;
  private int count;

  public OrderGoods() {
  }

  public OrderGoods(long id, String name, String image, double price, int count) {
    this.id = id;
    this.name = name;
    this.image = image;
    this.price = price;
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

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
