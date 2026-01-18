package com.hzcu.order.entity;

import java.math.BigDecimal;
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
@Table(name = "payment_record")
@EntityListeners(AuditingEntityListener.class)
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "pay_no", length = 100)
    private String payNo;

    @Column(length = 20)
    private String channel; // WECHAT

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 20)
    private String status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "raw_response", columnDefinition = "JSON")
    private String rawResponse;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PaymentRecord() {}

    public PaymentRecord(Long id, Order order, String payNo, String channel, BigDecimal amount, String status, LocalDateTime paidAt, String rawResponse, LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.payNo = payNo;
        this.channel = channel;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
        this.rawResponse = rawResponse;
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

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static PaymentRecordBuilder builder() {
        return new PaymentRecordBuilder();
    }

    public static class PaymentRecordBuilder {
        private Long id;
        private Order order;
        private String payNo;
        private String channel;
        private BigDecimal amount;
        private String status;
        private LocalDateTime paidAt;
        private String rawResponse;
        private LocalDateTime createdAt;

        public PaymentRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PaymentRecordBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public PaymentRecordBuilder payNo(String payNo) {
            this.payNo = payNo;
            return this;
        }

        public PaymentRecordBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public PaymentRecordBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentRecordBuilder status(String status) {
            this.status = status;
            return this;
        }

        public PaymentRecordBuilder paidAt(LocalDateTime paidAt) {
            this.paidAt = paidAt;
            return this;
        }

        public PaymentRecordBuilder rawResponse(String rawResponse) {
            this.rawResponse = rawResponse;
            return this;
        }

        public PaymentRecordBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PaymentRecord build() {
            return new PaymentRecord(id, order, payNo, channel, amount, status, paidAt, rawResponse, createdAt);
        }
    }
}
