package com.atharva.erp_telecom.dto;


import java.math.BigDecimal;
import java.util.List;

public class OrderCheckoutRequest {

    private Long customerId;
    private List<OrderProductRequest> orderProducts;

    private String promoCode;
    private String currencyCode;      // e.g., "INR", "USD"
    private String remarks;           // optional note by user or sales agent
    private boolean autoGenerateInvoice ; // default true
    private BigDecimal shippingCharges; // optional for hardware delivery
    private BigDecimal discountAmount;  // total discount

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderProductRequest> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductRequest> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isAutoGenerateInvoice() {
        return autoGenerateInvoice;
    }

    public void setAutoGenerateInvoice(boolean autoGenerateInvoice) {
        this.autoGenerateInvoice = autoGenerateInvoice;
    }

    public BigDecimal getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(BigDecimal shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}
