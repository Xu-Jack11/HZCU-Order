package com.hzcu.order.model;

import java.util.List;

public class Order {

  private long id;
  private long userId;
  private long shopId;
  private String shopName;
  private String shopLogo;
  private List<OrderGoods> goods;
  private int totalCount;
  private double totalPrice;
  private String status;
  private String statusText;
  private String createTime;
  private String diningMode;
  private String tableNo;
  private String pickupTime;
  private String remark;

  public Order() {
  }

  public Order(long id, long shopId, String shopName, String shopLogo, List<OrderGoods> goods, int totalCount,
      double totalPrice, String status, String statusText, String createTime, String diningMode, String tableNo,
      String pickupTime, String remark) {
    this.id = id;
    this.shopId = shopId;
    this.shopName = shopName;
    this.shopLogo = shopLogo;
    this.goods = goods;
    this.totalCount = totalCount;
    this.totalPrice = totalPrice;
    this.status = status;
    this.statusText = statusText;
    this.createTime = createTime;
    this.diningMode = diningMode;
    this.tableNo = tableNo;
    this.pickupTime = pickupTime;
    this.remark = remark;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getShopId() {
    return shopId;
  }

  public void setShopId(long shopId) {
    this.shopId = shopId;
  }

  public String getShopName() {
    return shopName;
  }

  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  public String getShopLogo() {
    return shopLogo;
  }

  public void setShopLogo(String shopLogo) {
    this.shopLogo = shopLogo;
  }

  public List<OrderGoods> getGoods() {
    return goods;
  }

  public void setGoods(List<OrderGoods> goods) {
    this.goods = goods;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getDiningMode() {
    return diningMode;
  }

  public void setDiningMode(String diningMode) {
    this.diningMode = diningMode;
  }

  public String getTableNo() {
    return tableNo;
  }

  public void setTableNo(String tableNo) {
    this.tableNo = tableNo;
  }

  public String getPickupTime() {
    return pickupTime;
  }

  public void setPickupTime(String pickupTime) {
    this.pickupTime = pickupTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
