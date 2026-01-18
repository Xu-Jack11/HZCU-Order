package com.hzcu.order.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "order_no", length = 50, unique = true)
    private String orderNo;

    @Column(length = 20)
    private String status;

    @Column(name = "dining_mode", length = 20)
    private String diningMode; // DINE_IN, TAKEAWAY

    @Column(name = "reserve_start")
    private LocalDateTime reserveStart;

    @Column(name = "reserve_end")
    private LocalDateTime reserveEnd;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "package_fee", precision = 10, scale = 2)
    private BigDecimal packageFee = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 10, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "pickup_code", length = 10)
    private String pickupCode;

    @Column(name = "pickup_window", length = 50)
    private String pickupWindow;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    public Order() {}

    public Order(Long orderId, User user, Canteen canteen, Long reviewId, String orderNo, String status, String diningMode, LocalDateTime reserveStart, LocalDateTime reserveEnd, BigDecimal totalAmount, BigDecimal packageFee, BigDecimal discountAmount, BigDecimal paidAmount, String paymentMethod, String pickupCode, String pickupWindow, String remark, String cancelReason, LocalDateTime createdAt, LocalDateTime updatedAt, List<OrderItem> items) {
        this.orderId = orderId;
        this.user = user;
        this.canteen = canteen;
        this.reviewId = reviewId;
        this.orderNo = orderNo;
        this.status = status;
        this.diningMode = diningMode;
        this.reserveStart = reserveStart;
        this.reserveEnd = reserveEnd;
        this.totalAmount = totalAmount;
        this.packageFee = packageFee;
        this.discountAmount = discountAmount;
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.pickupCode = pickupCode;
        this.pickupWindow = pickupWindow;
        this.remark = remark;
        this.cancelReason = cancelReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Canteen getCanteen() {
        return canteen;
    }

    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Long orderId;
        private User user;
        private Canteen canteen;
        private Long reviewId;
        private String orderNo;
        private String status;
        private String diningMode;
        private LocalDateTime reserveStart;
        private LocalDateTime reserveEnd;
        private BigDecimal totalAmount;
        private BigDecimal packageFee = BigDecimal.ZERO;
        private BigDecimal discountAmount = BigDecimal.ZERO;
        private BigDecimal paidAmount;
        private String paymentMethod;
        private String pickupCode;
        private String pickupWindow;
        private String remark;
        private String cancelReason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<OrderItem> items;

        public OrderBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder user(User user) {
            this.user = user;
            return this;
        }

        public OrderBuilder canteen(Canteen canteen) {
            this.canteen = canteen;
            return this;
        }

        public OrderBuilder reviewId(Long reviewId) {
            this.reviewId = reviewId;
            return this;
        }

        public OrderBuilder orderNo(String orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public OrderBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrderBuilder diningMode(String diningMode) {
            this.diningMode = diningMode;
            return this;
        }

        public OrderBuilder reserveStart(LocalDateTime reserveStart) {
            this.reserveStart = reserveStart;
            return this;
        }

        public OrderBuilder reserveEnd(LocalDateTime reserveEnd) {
            this.reserveEnd = reserveEnd;
            return this;
        }

        public OrderBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderBuilder packageFee(BigDecimal packageFee) {
            this.packageFee = packageFee;
            return this;
        }

        public OrderBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public OrderBuilder paidAmount(BigDecimal paidAmount) {
            this.paidAmount = paidAmount;
            return this;
        }

        public OrderBuilder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public OrderBuilder pickupCode(String pickupCode) {
            this.pickupCode = pickupCode;
            return this;
        }

        public OrderBuilder pickupWindow(String pickupWindow) {
            this.pickupWindow = pickupWindow;
            return this;
        }

        public OrderBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public OrderBuilder cancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
            return this;
        }

        public OrderBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OrderBuilder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Order build() {
            return new Order(orderId, user, canteen, reviewId, orderNo, status, diningMode, reserveStart, reserveEnd, totalAmount, packageFee, discountAmount, paidAmount, paymentMethod, pickupCode, pickupWindow, remark, cancelReason, createdAt, updatedAt, items);
        }
    }
}
