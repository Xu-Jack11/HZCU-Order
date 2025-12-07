package com.hzcu.order.model;

import java.util.List;

public class Shop {

  private long id;
  private String name;
  private String logo;
  private double rating;
  private int monthlySales;
  private int waitTime;
  private String distance;
  private double minPrice;
  private List<String> tags;
  private List<Integer> categoryIds;
  private String notice;
  private String address;
  private String businessHours;
  private String phone;

  public Shop() {
  }

  public Shop(long id, String name, String logo, double rating, int monthlySales, int waitTime, String distance,
      double minPrice, List<String> tags, List<Integer> categoryIds, String notice, String address,
      String businessHours, String phone) {
    this.id = id;
    this.name = name;
    this.logo = logo;
    this.rating = rating;
    this.monthlySales = monthlySales;
    this.waitTime = waitTime;
    this.distance = distance;
    this.minPrice = minPrice;
    this.tags = tags;
    this.categoryIds = categoryIds;
    this.notice = notice;
    this.address = address;
    this.businessHours = businessHours;
    this.phone = phone;
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

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getMonthlySales() {
    return monthlySales;
  }

  public void setMonthlySales(int monthlySales) {
    this.monthlySales = monthlySales;
  }

  public int getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(int waitTime) {
    this.waitTime = waitTime;
  }

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(double minPrice) {
    this.minPrice = minPrice;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public List<Integer> getCategoryIds() {
    return categoryIds;
  }

  public void setCategoryIds(List<Integer> categoryIds) {
    this.categoryIds = categoryIds;
  }

  public String getNotice() {
    return notice;
  }

  public void setNotice(String notice) {
    this.notice = notice;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getBusinessHours() {
    return businessHours;
  }

  public void setBusinessHours(String businessHours) {
    this.businessHours = businessHours;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
