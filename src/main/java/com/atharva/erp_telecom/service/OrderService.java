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
import com.atharva.erp_telecom.utils.GenericUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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

    @Transactional
    public Order createOrder(OrderCheckoutRequest orderCheckoutRequest) {
        Long customerId = orderCheckoutRequest.getCustomerId();
        // Calling the Product repository only once to stop multiple queries.
        List<Product> products =
                productRepository.findAllById(orderCheckoutRequest.getOrderProducts()
                        .stream()
                        .map(OrderProductRequest::getProductId)
                        .toList());

        Map<Long,Product> productMap = products.stream()
                                        .collect(Collectors.toMap(Product::getProductId,product -> product));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for ID: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderNumber(EntityNumberGeneratorUtil.generateOrderNumber(customerId));
        order.setCreatedBy("PHOTON_ERP");
        order.setUpdatedBy("PHOTON_ERP");
        order.setStatus(OrderStatus.CREATED);
        order.setOrderType(GenericUtils.deriveOrderTypeFromChargePlan(products));
        orderItemService.createOrderItems(order,orderCheckoutRequest,productMap);
        order.setTotalAmount(calculateOrderItemTotalAmount(order.getItems()));
        Order savedOrder = orderRepository.saveAndFlush(order);
        String productNames = products.stream()
                .map(Product::getProductName)
                .collect(Collectors.joining(", "));
        savedOrder.setRemarks(String.format("Order created successfully for Customer: %s | Products: %s | Type: %s | Order Id: %s"
                ,customerId,productNames,savedOrder.getOrderType(),savedOrder.getOrderId()));
        invoiceService.createInvoiceFromOrder(savedOrder.getOrderId());
        // Instead of --> return orderRepository.save(savedOrder), we will proceed by simply returning the
        // savedOrder since Hibernates Persistence Context will flush and save the order from the first save() call (no need for a second call)
        return savedOrder;
    }

    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Helper Method:
    private BigDecimal calculateOrderItemTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}