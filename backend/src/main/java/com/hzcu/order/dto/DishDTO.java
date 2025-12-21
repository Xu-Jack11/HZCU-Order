package com.hzcu.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DishDTO {
    private Long dishId;
    private Long canteenId;
    private Long categoryId;
    private String name;
    private String description;
    @com.fasterxml.jackson.annotation.JsonProperty("imageUrl")
    private String coverImage;
    @com.fasterxml.jackson.annotation.JsonProperty("sales")
    private Integer monthSales;
    @com.fasterxml.jackson.annotation.JsonProperty("price")
    private BigDecimal basePrice;
    private Integer status;
    private String categoryName;
}
