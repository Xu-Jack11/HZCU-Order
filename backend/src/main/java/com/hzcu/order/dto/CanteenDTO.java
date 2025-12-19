package com.hzcu.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CanteenDTO {
    private Long canteenId;
    private String name;
    private String campus;
    private String location;
    private String contactPhone;
    private Integer status;
    private String businessHours;
    private BigDecimal serviceFeeRate;
    private String remark;
    private String imageUrl;
}
