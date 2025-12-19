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
    private String coverImage;
    private Integer monthSales;
    private BigDecimal basePrice;
    private Integer status;
    private String categoryName;
}
