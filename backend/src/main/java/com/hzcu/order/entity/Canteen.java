package com.hzcu.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "canteen")
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

    public Canteen() {}

    public Canteen(Long canteenId, String name, String campus, String location, String contactPhone, Integer status, String businessHours, BigDecimal serviceFeeRate, String remark, Integer sortOrder, String imageUrl, Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.canteenId = canteenId;
        this.name = name;
        this.campus = campus;
        this.location = location;
        this.contactPhone = contactPhone;
        this.status = status;
        this.businessHours = businessHours;
        this.serviceFeeRate = serviceFeeRate;
        this.remark = remark;
        this.sortOrder = sortOrder;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCanteenId() { return canteenId; }
    public void setCanteenId(Long canteenId) { this.canteenId = canteenId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }
    public BigDecimal getServiceFeeRate() { return serviceFeeRate; }
    public void setServiceFeeRate(BigDecimal serviceFeeRate) { this.serviceFeeRate = serviceFeeRate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static CanteenBuilder builder() {
        return new CanteenBuilder();
    }

    public static class CanteenBuilder {
        private Long canteenId;
        private String name;
        private String campus;
        private String location;
        private String contactPhone;
        private Integer status;
        private String businessHours;
        private BigDecimal serviceFeeRate;
        private String remark;
        private Integer sortOrder;
        private String imageUrl;
        private Boolean isDeleted;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CanteenBuilder canteenId(Long canteenId) { this.canteenId = canteenId; return this; }
        public CanteenBuilder name(String name) { this.name = name; return this; }
        public CanteenBuilder campus(String campus) { this.campus = campus; return this; }
        public CanteenBuilder location(String location) { this.location = location; return this; }
        public CanteenBuilder contactPhone(String contactPhone) { this.contactPhone = contactPhone; return this; }
        public CanteenBuilder status(Integer status) { this.status = status; return this; }
        public CanteenBuilder businessHours(String businessHours) { this.businessHours = businessHours; return this; }
        public CanteenBuilder serviceFeeRate(BigDecimal serviceFeeRate) { this.serviceFeeRate = serviceFeeRate; return this; }
        public CanteenBuilder remark(String remark) { this.remark = remark; return this; }
        public CanteenBuilder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }
        public CanteenBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public CanteenBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public CanteenBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CanteenBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Canteen build() {
            return new Canteen(canteenId, name, campus, location, contactPhone, status, businessHours, serviceFeeRate, remark, sortOrder, imageUrl, isDeleted, createdAt, updatedAt);
        }
    }
}
