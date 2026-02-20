package com.atharva.erp_telecom.salesorder.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderItemResponse {
    private long orderItemId;
    private String orderLineItemNumber;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal unitPrice;
    // Add contract when in place.

}
