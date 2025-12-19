package com.hzcu.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateMerchantRequest {
    // Canteen details
    private String name;
    private String campus;
    private String location;
    private String contactPhone;
    private String businessHours;
    private BigDecimal serviceFeeRate;
    private String remark;
    private String imageUrl;

    // Admin account details
    private String username;
    private String password;
    private String realName;
    private String mobile;
}
