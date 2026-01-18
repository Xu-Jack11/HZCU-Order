package com.hzcu.order.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log")
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator_type", length = 20)
    private String operatorType;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(length = 100)
    private String action;

    @Column(name = "request_path", length = 500)
    private String requestPath;

    @Column(columnDefinition = "JSON")
    private String changes;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AuditLog() {}

    public AuditLog(Long id, String operatorType, Long operatorId, String action, String requestPath, String changes, String ipAddress, LocalDateTime createdAt) {
        this.id = id;
        this.operatorType = operatorType;
        this.operatorId = operatorId;
        this.action = action;
        this.requestPath = requestPath;
        this.changes = changes;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    public static class AuditLogBuilder {
        private Long id;
        private String operatorType;
        private Long operatorId;
        private String action;
        private String requestPath;
        private String changes;
        private String ipAddress;
        private LocalDateTime createdAt;

        public AuditLogBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AuditLogBuilder operatorType(String operatorType) {
            this.operatorType = operatorType;
            return this;
        }

        public AuditLogBuilder operatorId(Long operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public AuditLogBuilder action(String action) {
            this.action = action;
            return this;
        }

        public AuditLogBuilder requestPath(String requestPath) {
            this.requestPath = requestPath;
            return this;
        }

        public AuditLogBuilder changes(String changes) {
            this.changes = changes;
            return this;
        }

        public AuditLogBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public AuditLogBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AuditLog build() {
            return new AuditLog(id, operatorType, operatorId, action, requestPath, changes, ipAddress, createdAt);
        }
    }
}
