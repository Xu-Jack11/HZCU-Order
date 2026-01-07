package com.hzcu.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dish_spec")
@EntityListeners(AuditingEntityListener.class)
public class DishSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status; // 1:正常 0:禁用

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DishSpec() {}

    public DishSpec(Long id, Dish dish, String name, BigDecimal price, Integer sortOrder, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dish = dish;
        this.name = name;
        this.price = price;
        this.sortOrder = sortOrder;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public static DishSpecBuilder builder() {
        return new DishSpecBuilder();
    }

    public static class DishSpecBuilder {
        private Long id;
        private Dish dish;
        private String name;
        private BigDecimal price;
        private Integer sortOrder;
        private Integer status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DishSpecBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DishSpecBuilder dish(Dish dish) {
            this.dish = dish;
            return this;
        }

        public DishSpecBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DishSpecBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public DishSpecBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public DishSpecBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public DishSpecBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DishSpecBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DishSpec build() {
            return new DishSpec(id, dish, name, price, sortOrder, status, createdAt, updatedAt);
        }
    }
}
