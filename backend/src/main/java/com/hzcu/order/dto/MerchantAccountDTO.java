package com.hzcu.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MerchantAccountDTO {
    private Long merchantAccountId;
    private Long canteenId;
    private String username;
    private String realName;
    private String mobile;
    private String role;
    private Integer status;
    private LocalDateTime lastLoginAt;
}
