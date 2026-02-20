package com.atharva.erp_telecom.salesorder.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderProductRequest {
    private Long productId;
    private int quantity;
    private Long chargePlanId;
}
