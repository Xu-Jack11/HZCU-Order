package com.hzcu.order.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "dish_name", length = 200)
    private String dishName;

    @Column(name = "spec_name", length = 100)
    private String specName;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    private Integer quantity = 1;

    @Column(name = "extra_options", columnDefinition = "JSON")
    private String extraOptions; // Store as JSON string or use a converter

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    public OrderItem() {}

    public OrderItem(Long id, Order order, Dish dish, String dishName, String specName, BigDecimal unitPrice, Integer quantity, String extraOptions, BigDecimal totalPrice) {
        this.id = id;
        this.order = order;
        this.dish = dish;
        this.dishName = dishName;
        this.specName = specName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.extraOptions = extraOptions;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(String extraOptions) {
        this.extraOptions = extraOptions;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public static class OrderItemBuilder {
        private Long id;
        private Order order;
        private Dish dish;
        private String dishName;
        private String specName;
        private BigDecimal unitPrice;
        private Integer quantity = 1;
        private String extraOptions;
        private BigDecimal totalPrice;

        public OrderItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderItemBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderItemBuilder dish(Dish dish) {
            this.dish = dish;
            return this;
        }

        public OrderItemBuilder dishName(String dishName) {
            this.dishName = dishName;
            return this;
        }

        public OrderItemBuilder specName(String specName) {
            this.specName = specName;
            return this;
        }

        public OrderItemBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public OrderItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder extraOptions(String extraOptions) {
            this.extraOptions = extraOptions;
            return this;
        }

        public OrderItemBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(id, order, dish, dishName, specName, unitPrice, quantity, extraOptions, totalPrice);
        }
    }
}
