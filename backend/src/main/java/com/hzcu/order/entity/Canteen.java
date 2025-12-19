package com.hzcu.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "canteen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Canteen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canteen_id")
    private Long canteenId;

    @Column(length = 200)
    private String name;

    @Column(length = 100)
    private String campus;

    @Column(length = 200)
    private String location;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status; // 1:营业 0:关闭

    @Column(name = "business_hours", length = 100)
    private String businessHours;

    @Column(name = "service_fee_rate", precision = 5, scale = 4)
    private BigDecimal serviceFeeRate;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(name = "sort_order", columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_deleted", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
