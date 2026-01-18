package com.hzcu.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DishCategoryDTO {
    private Long id;
    private Long canteenId;
    private String name;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private List<DishDTO> dishes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCanteenId() {
        return canteenId;
    }

    public void setCanteenId(Long canteenId) {
        this.canteenId = canteenId;
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

    public List<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }
}
