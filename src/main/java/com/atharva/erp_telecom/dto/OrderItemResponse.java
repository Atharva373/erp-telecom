package com.atharva.erp_telecom.dto;

import com.atharva.erp_telecom.entity.Product;

import java.math.BigDecimal;

public class OrderItemResponse {
    private long orderItemId;
    private String orderLineItemNumber;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal unitPrice;
    // Add contract when in place.


    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderLineItemNumber() {
        return orderLineItemNumber;
    }

    public void setOrderLineItemNumber(String orderLineItemNumber) {
        this.orderLineItemNumber = orderLineItemNumber;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
