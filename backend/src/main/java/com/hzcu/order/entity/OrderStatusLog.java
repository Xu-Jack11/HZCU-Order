package com.hzcu.order.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "order_status_log")
@EntityListeners(AuditingEntityListener.class)
public class OrderStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "from_status", length = 20)
    private String fromStatus;

    @Column(name = "to_status", length = 20)
    private String toStatus;

    @Column(name = "operator_type", length = 20)
    private String operatorType;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OrderStatusLog() {}

    public OrderStatusLog(Long id, Order order, String fromStatus, String toStatus, String operatorType, Long operatorId, String remark, LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.operatorType = operatorType;
        this.operatorId = operatorId;
        this.remark = remark;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static OrderStatusLogBuilder builder() {
        return new OrderStatusLogBuilder();
    }

    public static class OrderStatusLogBuilder {
        private Long id;
        private Order order;
        private String fromStatus;
        private String toStatus;
        private String operatorType;
        private Long operatorId;
        private String remark;
        private LocalDateTime createdAt;

        public OrderStatusLogBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderStatusLogBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderStatusLogBuilder fromStatus(String fromStatus) {
            this.fromStatus = fromStatus;
            return this;
        }

        public OrderStatusLogBuilder toStatus(String toStatus) {
            this.toStatus = toStatus;
            return this;
        }

        public OrderStatusLogBuilder operatorType(String operatorType) {
            this.operatorType = operatorType;
            return this;
        }

        public OrderStatusLogBuilder operatorId(Long operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public OrderStatusLogBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public OrderStatusLogBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderStatusLog build() {
            return new OrderStatusLog(id, order, fromStatus, toStatus, operatorType, operatorId, remark, createdAt);
        }
    }
}
