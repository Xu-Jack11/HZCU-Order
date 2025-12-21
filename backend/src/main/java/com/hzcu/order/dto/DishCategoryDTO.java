package com.hzcu.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DishCategoryDTO {
    private Long id;
    private Long canteenId;
    private String name;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private java.util.List<DishDTO> dishes;
}
