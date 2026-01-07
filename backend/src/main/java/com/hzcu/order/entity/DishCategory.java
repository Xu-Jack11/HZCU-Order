package com.hzcu.order.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "dish_category")
@EntityListeners(AuditingEntityListener.class)
public class DishCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;

    @Column(length = 100, nullable = false)
    private String name;

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

    public DishCategory() {}

    public DishCategory(Long id, Canteen canteen, String name, Integer sortOrder, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.canteen = canteen;
        this.name = name;
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

    public Canteen getCanteen() {
        return canteen;
    }

    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static DishCategoryBuilder builder() {
        return new DishCategoryBuilder();
    }

    public static class DishCategoryBuilder {
        private Long id;
        private Canteen canteen;
        private String name;
        private Integer sortOrder;
        private Integer status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DishCategoryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DishCategoryBuilder canteen(Canteen canteen) {
            this.canteen = canteen;
            return this;
        }

        public DishCategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DishCategoryBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public DishCategoryBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public DishCategoryBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DishCategoryBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DishCategory build() {
            return new DishCategory(id, canteen, name, sortOrder, status, createdAt, updatedAt);
        }
    }
}
