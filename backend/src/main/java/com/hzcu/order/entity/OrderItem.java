package com.hzcu.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    private Integer quantity = 1;

    @Column(name = "extra_options", columnDefinition = "JSON")
    private String extraOptions; // Store as JSON string or use a converter

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
