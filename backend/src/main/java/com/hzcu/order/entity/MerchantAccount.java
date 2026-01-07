package com.hzcu.order.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "merchant_account")
@EntityListeners(AuditingEntityListener.class)
public class MerchantAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_account_id")
    private Long merchantAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;

    @Column(length = 50, unique = true)
    private String username;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "real_name", length = 100)
    private String realName;

    @Column(length = 20)
    private String mobile;

    @Column(length = 20)
    private String role; // ADMIN, OPERATOR

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status; // 1:正常 0:禁用

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MerchantAccount() {}

    public MerchantAccount(Long merchantAccountId, Canteen canteen, String username, String passwordHash, String realName, String mobile, String role, Integer status, LocalDateTime lastLoginAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.merchantAccountId = merchantAccountId;
        this.canteen = canteen;
        this.username = username;
        this.passwordHash = passwordHash;
        this.realName = realName;
        this.mobile = mobile;
        this.role = role;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getMerchantAccountId() { return merchantAccountId; }
    public void setMerchantAccountId(Long merchantAccountId) { this.merchantAccountId = merchantAccountId; }
    public Canteen getCanteen() { return canteen; }
    public void setCanteen(Canteen canteen) { this.canteen = canteen; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static MerchantAccountBuilder builder() {
        return new MerchantAccountBuilder();
    }

    public static class MerchantAccountBuilder {
        private Long merchantAccountId;
        private Canteen canteen;
        private String username;
        private String passwordHash;
        private String realName;
        private String mobile;
        private String role;
        private Integer status;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public MerchantAccountBuilder merchantAccountId(Long merchantAccountId) { this.merchantAccountId = merchantAccountId; return this; }
        public MerchantAccountBuilder canteen(Canteen canteen) { this.canteen = canteen; return this; }
        public MerchantAccountBuilder username(String username) { this.username = username; return this; }
        public MerchantAccountBuilder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public MerchantAccountBuilder realName(String realName) { this.realName = realName; return this; }
        public MerchantAccountBuilder mobile(String mobile) { this.mobile = mobile; return this; }
        public MerchantAccountBuilder role(String role) { this.role = role; return this; }
        public MerchantAccountBuilder status(Integer status) { this.status = status; return this; }
        public MerchantAccountBuilder lastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; return this; }
        public MerchantAccountBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public MerchantAccountBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public MerchantAccount build() {
            return new MerchantAccount(merchantAccountId, canteen, username, passwordHash, realName, mobile, role, status, lastLoginAt, createdAt, updatedAt);
        }
    }
}
