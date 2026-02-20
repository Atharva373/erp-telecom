package com.atharva.erp_telecom.salesorder.dto;

import com.atharva.erp_telecom.invoicing.dto.ChargePlanResponse;

public class ProductResponse {
    private Long productId;
    private String productCode;     // Uniques codes for each product.
    private String productName;
    private String productDescription;
    private String productCategory;
    private ChargePlanResponse chargePlan;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public ChargePlanResponse getChargePlan() {
        return chargePlan;
    }

    public void setChargePlan(ChargePlanResponse chargePlan) {
        this.chargePlan = chargePlan;
    }
}
