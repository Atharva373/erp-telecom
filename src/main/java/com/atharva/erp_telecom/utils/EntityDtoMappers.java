package com.atharva.erp_telecom.utils;

import com.atharva.erp_telecom.dto.*;
import com.atharva.erp_telecom.entity.*;

import java.util.Collections;
import java.util.List;

public class EntityDtoMappers {

    public static OrderResponse mapOrderToOrderResponse(Order order){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setOrderStatus(order.getStatus());
        orderResponse.setOrderType(order.getOrderType());

        // Null check for Order items in an order.
        List<OrderItem> items = (order.getItems() != null)
                ? order.getItems()
                : Collections.emptyList();

        orderResponse.setOrderItems(
                order.getItems()
                        .stream()
                        .map(EntityDtoMappers::mapOrderItemToOrderItemResponse)
                        .toList()
        );
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setCustomerInfo(EntityDtoMappers.mapCustomerToCustomerInfoResponse(order.getCustomer()));
        orderResponse.setRemarks(order.getRemarks());
        orderResponse.setInvoice(EntityDtoMappers.mapInvoiceToInvoiceResponse(order.getInvoice()));
        return orderResponse;
    }

    public static OrderItemResponse mapOrderItemToOrderItemResponse(OrderItem orderItem){
        if (orderItem == null) return null;
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setOrderItemId(orderItem.getOrderItemId());
        orderItemResponse.setOrderLineItemNumber(orderItem.getOrderLineItemNumber());
        orderItemResponse.setProduct(EntityDtoMappers.mapProductToProductResponse(orderItem.getProduct()));
        orderItemResponse.setQuantity(orderItem.getQuantity());
        orderItemResponse.setPrice(orderItem.getPrice());
        orderItemResponse.setUnitPrice(orderItem.getUnitPrice());
        return orderItemResponse;
    }

    public static ProductResponse mapProductToProductResponse(Product product){
        if (product == null) return null;
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductCode(product.getProductCode());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductDescription(product.getProductDescription());
        productResponse.setProductCategory(product.getProductCategory());
        ChargePlan defaultPlan = GenericUtils.getDefaultChargePlan(product);
        productResponse.setChargePlan(
                EntityDtoMappers.mapChargePlanToChargePlanResponse(defaultPlan)
        );
        return productResponse;
    }

    public static ChargePlanResponse mapChargePlanToChargePlanResponse(ChargePlan chargePlan){
        if (chargePlan == null) return null;
        ChargePlanResponse chargePlanResponse = new ChargePlanResponse();
        chargePlanResponse.setChargePlanId(chargePlan.getChargePlanId());
        chargePlanResponse.setPlanType(chargePlan.getPlanType().toString());
        chargePlanResponse.setRecurringCharge(chargePlan.getRecurringCharge());
        chargePlanResponse.setOneOffCharge(chargePlan.getOneOffCharge());
        chargePlanResponse.setRatePerUnit(chargePlan.getRatePerUnit());
        chargePlanResponse.setValidityDays(chargePlan.getValidityDays());
        chargePlanResponse.setCurrency(chargePlan.getCurrency());
        return chargePlanResponse;

    }

    public static CustomerInfoResponse mapCustomerToCustomerInfoResponse(Customer customer){
        if (customer == null) return null;
        CustomerInfoResponse customerInfoResponse = new CustomerInfoResponse();
        customerInfoResponse.setCustomerId(customer.getCustomerId());
        customerInfoResponse.setFirstName(customer.getCustomerFirstName());
        customerInfoResponse.setLastName(customer.getCustomerLastName());
        customerInfoResponse.setFullName(customerInfoResponse.getLastName() + ", " + customerInfoResponse.getFirstName());
        customerInfoResponse.setCustomerClass(customer.getCustomerClass());
        customerInfoResponse.setCustomerSubClass(customer.getCustomerSubClass());
        customerInfoResponse.setEmail(customer.getEmail());
        customerInfoResponse.setContactNumber(customer.getContactNumber());
        customerInfoResponse.setRegion(customer.getRegion());
        customerInfoResponse.setGovernmentId(customer.getGovernmentId());
        return customerInfoResponse;
    }

    public static InvoiceResponse mapInvoiceToInvoiceResponse(Invoice invoice){
        if (invoice == null) return null;
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setInvoiceId(invoice.getInvoiceId());
        invoiceResponse.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceResponse.setSubtotal(invoice.getSubTotal());
        invoiceResponse.setTaxTotal(invoice.getTaxTotal());
        invoiceResponse.setTotalAmount(invoice.getTotalAmount());
        invoiceResponse.setInvoiceDate(invoice.getInvoiceDate());
        invoiceResponse.setDueDate(invoice.getDueDate());
        invoiceResponse.setRemarks(invoice.getRemarks());
        invoiceResponse.setPaymentStatus(invoice.getPaymentStatus());

        // Null check for Invoice items.
        List<InvoiceItem> items = (invoice.getInvoiceItems() != null)
                ? invoice.getInvoiceItems()
                : Collections.emptyList();

        invoiceResponse.setInvoiceItems(
                invoice.getInvoiceItems()
                        .stream()
                        .map(EntityDtoMappers::mapInvoiceItemToInvoiceItemResponse)
                        .toList()
        );
        return invoiceResponse;
    }

    public static InvoiceItemResponse mapInvoiceItemToInvoiceItemResponse(InvoiceItem invoiceItem){
        if (invoiceItem == null) return null;
        InvoiceItemResponse invoiceItemResponse = new InvoiceItemResponse();
        invoiceItemResponse.setInvoiceItemId(invoiceItem.getInvoiceItemId());
        invoiceItemResponse.setInvoiceItemNumber(invoiceItem.getInvoiceItemNumber());
        OrderItem orderItem = invoiceItem.getOrderItem();
        if (orderItem != null) {
            invoiceItemResponse.setOrderItemNumber(orderItem.getOrderLineItemNumber());
        }
        invoiceItemResponse.setQuantity(invoiceItem.getQuantity());
        invoiceItemResponse.setUnitPrice(invoiceItem.getUnitPrice());
        invoiceItemResponse.setBaseAmount(invoiceItem.getBaseAmount());
        invoiceItemResponse.setTaxAmount(invoiceItem.getTaxAmount());
        invoiceItemResponse.setTotalAmount(invoiceItem.getTotalAmount());
        invoiceItemResponse.setAdditionalInfo(invoiceItem.getAdditionalInfo());
        return invoiceItemResponse;
    }
}
