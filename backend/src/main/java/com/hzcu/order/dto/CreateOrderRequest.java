package com.hzcu.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderRequest {

  @NotNull
  private Long shopId;

  @NotEmpty
  @Valid
  private List<CartItem> cartList;

  @Min(0)
  private double totalPrice;

  @NotBlank
  private String diningMode;

  private String tableNo;
  private String pickupTime;
  private String remark;

  public Long getShopId() {
    return shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }

  public List<CartItem> getCartList() {
    return cartList;
  }

  public void setCartList(List<CartItem> cartList) {
    this.cartList = cartList;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
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

  public static class CartItem {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String image;

    @Min(0)
    private double price;

    @Min(0)
    private int count;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
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
}
