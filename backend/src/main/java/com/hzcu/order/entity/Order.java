package com.hzcu.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private BigDecimal packageFee = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
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
    private java.util.List<OrderItem> items;
}
