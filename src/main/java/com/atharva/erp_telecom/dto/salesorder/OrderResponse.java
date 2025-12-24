package com.atharva.erp_telecom.dto.salesorder;

import com.atharva.erp_telecom.dto.crm.CustomerInfoResponse;
import com.atharva.erp_telecom.dto.finance.InvoiceResponse;
import com.atharva.erp_telecom.enums.OrderStatus;
import com.atharva.erp_telecom.enums.OrderType;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private List<OrderItemResponse> orderItems;
    private BigDecimal totalAmount;
    private CustomerInfoResponse customerInfo;
    private String remarks;
    private InvoiceResponse invoice;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CustomerInfoResponse getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfoResponse customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public InvoiceResponse getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceResponse invoice) {
        this.invoice = invoice;
    }
}
