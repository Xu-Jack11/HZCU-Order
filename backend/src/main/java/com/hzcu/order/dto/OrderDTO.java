package com.hzcu.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long canteenId;
    private String canteenName;
    private String canteenLogo;
    private String orderNo;
    private String status;
    private String diningMode;
    private LocalDateTime reserveStart;
    private LocalDateTime reserveEnd;
    private BigDecimal totalAmount;
    private BigDecimal packageFee;
    private BigDecimal discountAmount;
    private BigDecimal paidAmount;
    private String paymentMethod;
    private String pickupCode;
    private String pickupWindow;
    private String remark;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCanteenId() {
        return canteenId;
    }

    public void setCanteenId(Long canteenId) {
        this.canteenId = canteenId;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public String getCanteenLogo() {
        return canteenLogo;
    }

    public void setCanteenLogo(String canteenLogo) {
        this.canteenLogo = canteenLogo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiningMode() {
        return diningMode;
    }

    public void setDiningMode(String diningMode) {
        this.diningMode = diningMode;
    }

    public LocalDateTime getReserveStart() {
        return reserveStart;
    }

    public void setReserveStart(LocalDateTime reserveStart) {
        this.reserveStart = reserveStart;
    }

    public LocalDateTime getReserveEnd() {
        return reserveEnd;
    }

    public void setReserveEnd(LocalDateTime reserveEnd) {
        this.reserveEnd = reserveEnd;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPackageFee() {
        return packageFee;
    }

    public void setPackageFee(BigDecimal packageFee) {
        this.packageFee = packageFee;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public String getPickupWindow() {
        return pickupWindow;
    }

    public void setPickupWindow(String pickupWindow) {
        this.pickupWindow = pickupWindow;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
