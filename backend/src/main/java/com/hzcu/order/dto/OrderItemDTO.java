package com.hzcu.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long dishId;
    private String dishName;
    private String specName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String extraOptions;
    private BigDecimal totalPrice;
}
