package com.hzcu.order.dto;

import java.time.LocalDateTime;

public class MerchantAccountDTO {
    private Long merchantAccountId;
    private Long canteenId;
    private String username;
    private String realName;
    private String mobile;
    private String role;
    private Integer status;
    private LocalDateTime lastLoginAt;

    public Long getMerchantAccountId() {
        return merchantAccountId;
    }

    public void setMerchantAccountId(Long merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
    }

    public Long getCanteenId() {
        return canteenId;
    }

    public void setCanteenId(Long canteenId) {
        this.canteenId = canteenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
