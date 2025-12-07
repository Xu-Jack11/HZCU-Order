package com.hzcu.order.model;

import java.util.List;

public class HomeFeed {

  private List<String> banners;
  private List<String> announcements;
  private List<Shop> recommendCanteens;

  public HomeFeed() {
  }

  public HomeFeed(List<String> banners, List<String> announcements, List<Shop> recommendCanteens) {
    this.banners = banners;
    this.announcements = announcements;
    this.recommendCanteens = recommendCanteens;
  }

  public List<String> getBanners() {
    return banners;
  }

  public void setBanners(List<String> banners) {
    this.banners = banners;
  }

  public List<String> getAnnouncements() {
    return announcements;
  }

  public void setAnnouncements(List<String> announcements) {
    this.announcements = announcements;
  }

  public List<Shop> getRecommendCanteens() {
    return recommendCanteens;
  }

  public void setRecommendCanteens(List<Shop> recommendCanteens) {
    this.recommendCanteens = recommendCanteens;
  }
}
