package com.hzcu.order.entity;

import java.time.LocalDateTime;

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
@Table(name = "system_param")
@EntityListeners(AuditingEntityListener.class)
public class SystemParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "param_key", length = 100, unique = true)
    private String paramKey;

    @Column(name = "param_value", columnDefinition = "TEXT")
    private String paramValue;

    @Column(length = 500)
    private String description;

    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SystemParam() {}

    public SystemParam(Long id, String paramKey, String paramValue, String description, Long updatedBy, LocalDateTime updatedAt) {
        this.id = id;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.description = description;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static SystemParamBuilder builder() {
        return new SystemParamBuilder();
    }

    public static class SystemParamBuilder {
        private Long id;
        private String paramKey;
        private String paramValue;
        private String description;
        private Long updatedBy;
        private LocalDateTime updatedAt;

        public SystemParamBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SystemParamBuilder paramKey(String paramKey) {
            this.paramKey = paramKey;
            return this;
        }

        public SystemParamBuilder paramValue(String paramValue) {
            this.paramValue = paramValue;
            return this;
        }

        public SystemParamBuilder description(String description) {
            this.description = description;
            return this;
        }

        public SystemParamBuilder updatedBy(Long updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public SystemParamBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public SystemParam build() {
            return new SystemParam(id, paramKey, paramValue, description, updatedBy, updatedAt);
        }
    }
}
