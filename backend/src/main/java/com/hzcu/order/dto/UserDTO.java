package com.hzcu.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long userId;
    private String openid;
    private String nickname;
    private String avatarUrl;
    private String mobile;
    private Integer status;
    private LocalDateTime lastLoginAt;
}
