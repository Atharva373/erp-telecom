package com.atharva.erp_telecom.dto;


public class OrderProductRequest {

    private Long productId;
    private int quantity;
    private Long chargePlanId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getChargePlanId() {
        return chargePlanId;
    }

    public void setChargePlanId(Long chargePlanId) {
        this.chargePlanId = chargePlanId;
    }
}
