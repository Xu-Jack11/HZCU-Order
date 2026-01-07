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
@Table(name = "dish_spec_option")
@EntityListeners(AuditingEntityListener.class)
public class DishSpecOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_id", nullable = false)
    private DishSpec spec;

    @Column(name = "option_type", length = 50)
    private String optionType;

    @Column(name = "option_name", length = 100)
    private String optionName;

    @Column(name = "extra_price", precision = 10, scale = 2)
    private BigDecimal extraPrice = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public DishSpecOption() {}

    public DishSpecOption(Long id, DishSpec spec, String optionType, String optionName, BigDecimal extraPrice, LocalDateTime createdAt) {
        this.id = id;
        this.spec = spec;
        this.optionType = optionType;
        this.optionName = optionName;
        this.extraPrice = extraPrice != null ? extraPrice : BigDecimal.ZERO;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DishSpec getSpec() {
        return spec;
    }

    public void setSpec(DishSpec spec) {
        this.spec = spec;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public BigDecimal getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(BigDecimal extraPrice) {
        this.extraPrice = extraPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static DishSpecOptionBuilder builder() {
        return new DishSpecOptionBuilder();
    }

    public static class DishSpecOptionBuilder {
        private Long id;
        private DishSpec spec;
        private String optionType;
        private String optionName;
        private BigDecimal extraPrice = BigDecimal.ZERO;
        private LocalDateTime createdAt;

        public DishSpecOptionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DishSpecOptionBuilder spec(DishSpec spec) {
            this.spec = spec;
            return this;
        }

        public DishSpecOptionBuilder optionType(String optionType) {
            this.optionType = optionType;
            return this;
        }

        public DishSpecOptionBuilder optionName(String optionName) {
            this.optionName = optionName;
            return this;
        }

        public DishSpecOptionBuilder extraPrice(BigDecimal extraPrice) {
            this.extraPrice = extraPrice;
            return this;
        }

        public DishSpecOptionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DishSpecOption build() {
            return new DishSpecOption(id, spec, optionType, optionName, extraPrice, createdAt);
        }
    }
}
