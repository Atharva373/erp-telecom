package com.atharva.erp_telecom.salesorder.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderCheckoutRequest {

    private Long businessEntityId;
    private List<OrderProductRequest> orderProducts;

    private String promoCode;
    private String currencyCode;      // e.g., "INR", "USD"
    private String remarks;           // optional note by user or sales agent
    private boolean autoGenerateInvoice; // default true
    private BigDecimal shippingCharges; // optional for hardware delivery
    private BigDecimal discountAmount;  // total discount

}
