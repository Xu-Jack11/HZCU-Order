package com.hzcu.order.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dish")
@EntityListeners(AuditingEntityListener.class)
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long dishId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private DishCategory category;

    @Column(length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image", columnDefinition = "TEXT")
    private String coverImage;

    @Column(name = "month_sales")
    private Integer monthSales = 0;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status; // 1:上架 0:下架

    @Column(name = "is_deleted", columnDefinition = "TINYINT DEFAULT 0")
    private Integer isDeleted = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Dish() {}

    public Dish(Long dishId, Canteen canteen, DishCategory category, String name, String description, String coverImage, Integer monthSales, BigDecimal basePrice, Integer status, Integer isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.dishId = dishId;
        this.canteen = canteen;
        this.category = category;
        this.name = name;
        this.description = description;
        this.coverImage = coverImage;
        this.monthSales = monthSales;
        this.basePrice = basePrice;
        this.status = status;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Canteen getCanteen() {
        return canteen;
    }

    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }

    public DishCategory getCategory() {
        return category;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Integer monthSales) {
        this.monthSales = monthSales;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public static DishBuilder builder() {
        return new DishBuilder();
    }

    public static class DishBuilder {
        private Long dishId;
        private Canteen canteen;
        private DishCategory category;
        private String name;
        private String description;
        private String coverImage;
        private Integer monthSales = 0;
        private BigDecimal basePrice;
        private Integer status;
        private Integer isDeleted = 0;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DishBuilder dishId(Long dishId) {
            this.dishId = dishId;
            return this;
        }

        public DishBuilder canteen(Canteen canteen) {
            this.canteen = canteen;
            return this;
        }

        public DishBuilder category(DishCategory category) {
            this.category = category;
            return this;
        }

        public DishBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DishBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DishBuilder coverImage(String coverImage) {
            this.coverImage = coverImage;
            return this;
        }

        public DishBuilder monthSales(Integer monthSales) {
            this.monthSales = monthSales;
            return this;
        }

        public DishBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public DishBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public DishBuilder isDeleted(Integer isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public DishBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DishBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Dish build() {
            return new Dish(dishId, canteen, category, name, description, coverImage, monthSales, basePrice, status, isDeleted, createdAt, updatedAt);
        }
    }
}
