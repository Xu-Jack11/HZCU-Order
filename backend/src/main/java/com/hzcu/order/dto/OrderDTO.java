package com.hzcu.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long canteenId;
    private String canteenName;
    private String canteenLogo;
    private String orderNo;
    private String status;
    private String diningMode;
    private LocalDateTime reserveStart;
    private LocalDateTime reserveEnd;
    private BigDecimal totalAmount;
    private BigDecimal packageFee;
    private BigDecimal discountAmount;
    private BigDecimal paidAmount;
    private String paymentMethod;
    private String pickupCode;
    private String pickupWindow;
    private String remark;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
