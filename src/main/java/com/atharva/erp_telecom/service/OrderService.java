package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.dto.OrderCheckoutRequest;
import com.atharva.erp_telecom.dto.OrderProductRequest;
import com.atharva.erp_telecom.entity.*;
import com.atharva.erp_telecom.enums.OrderStatus;
import com.atharva.erp_telecom.enums.OrderType;
import com.atharva.erp_telecom.enums.PlanType;
import com.atharva.erp_telecom.exception.custom_exceptions.ChargePlanNotFoundException;
import com.atharva.erp_telecom.exception.custom_exceptions.ProductNotFoundException;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.CustomerRepository;
import com.atharva.erp_telecom.repository.OrderRepository;
import com.atharva.erp_telecom.repository.ProductRepository;
import com.atharva.erp_telecom.service.implementation.ProductServiceImplementation;
import com.atharva.erp_telecom.utils.EntityNumberGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, OrderItemService orderItemService, ProductService productService, InvoiceService invoiceService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.productRepository = productRepository;
    }

    public Order createOrder(OrderCheckoutRequest orderCheckoutRequest) {
        Long customerId = orderCheckoutRequest.getCustomerId();
        // Calling the Product repository only once to stop multiple queries.
        List<Product> products =
                productRepository.findAllById(orderCheckoutRequest.getOrderProducts()
                        .stream()
                        .map(OrderProductRequest::getProductId)
                        .toList());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for ID: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderNumber(EntityNumberGeneratorUtil.generateOrderNumber(customerId));
        order.setCreatedBy("PHOTON_ERP");
        order.setUpdatedBy("PHOTON_ERP");
        order.setStatus(OrderStatus.CREATED);
        order.setOrderType(deriveOrderTypeFromChargePlan(products));
        order.setItems(orderItemService.createOrderItems(order, orderCheckoutRequest,products));
        order.setTotalAmount(calculateOrderItemTotalAmount(order.getItems()));
        Order savedOrder = orderRepository.save(order);
        String productNames = products.stream()
                .map(Product::getProductName)
                .collect(Collectors.joining(", "));
        savedOrder.setRemarks(String.format("Order created successfully for Customer: %s | Products: %s | Type: %s | Order Id: %s"
                ,customerId,productNames,savedOrder.getOrderType(),savedOrder.getOrderId()));
        savedOrder.setInvoice(invoiceService.createInvoiceFromOrder(savedOrder.getOrderId()));
        return orderRepository.save(savedOrder);
    }



    // Helper Methods:
    private OrderType deriveOrderTypeFromChargePlan(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("No products found in the order");
        }

        Set<PlanType> chargeTypes = products.stream()
                .flatMap(product -> product.getChargePlans().stream())
                .filter(ChargePlan::isDefault)
                .map(ChargePlan::getPlanType)
                .collect(Collectors.toSet());

        if (chargeTypes.isEmpty()) {
            throw new ChargePlanNotFoundException("No Charge Plans found for provided products");
        }

        if (chargeTypes.size() == 1) {
            PlanType singleType = chargeTypes.iterator().next();
            return (singleType == PlanType.PREPAID) ? OrderType.PREPAID : OrderType.POSTPAID;
        }
        throw new IllegalArgumentException("Mixed PREPAID and POSTPAID products not allowed in the same order");
    }

    private BigDecimal calculateOrderItemTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}