package com.hzcu.order.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_user")
@EntityListeners(AuditingEntityListener.class)
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(length = 50, unique = true)
    private String username;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "real_name", length = 100)
    private String realName;

    @Column(length = 20)
    private String mobile;

    @Column(length = 100)
    private String email;

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

    public AdminUser() {}

    public AdminUser(Long adminId, String username, String passwordHash, String realName, String mobile, String email, Integer status, LocalDateTime lastLoginAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.adminId = adminId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.realName = realName;
        this.mobile = mobile;
        this.email = email;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static AdminUserBuilder builder() {
        return new AdminUserBuilder();
    }

    public static class AdminUserBuilder {
        private Long adminId;
        private String username;
        private String passwordHash;
        private String realName;
        private String mobile;
        private String email;
        private Integer status;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AdminUserBuilder adminId(Long adminId) { this.adminId = adminId; return this; }
        public AdminUserBuilder username(String username) { this.username = username; return this; }
        public AdminUserBuilder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public AdminUserBuilder realName(String realName) { this.realName = realName; return this; }
        public AdminUserBuilder mobile(String mobile) { this.mobile = mobile; return this; }
        public AdminUserBuilder email(String email) { this.email = email; return this; }
        public AdminUserBuilder status(Integer status) { this.status = status; return this; }
        public AdminUserBuilder lastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; return this; }
        public AdminUserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AdminUserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AdminUser build() {
            return new AdminUser(adminId, username, passwordHash, realName, mobile, email, status, lastLoginAt, createdAt, updatedAt);
        }
    }
}
