package com.atharva.erp_telecom.service.salesorder;

import com.atharva.erp_telecom.dto.salesorder.OrderCheckoutRequest;
import com.atharva.erp_telecom.dto.salesorder.OrderProductRequest;
import com.atharva.erp_telecom.dto.salesorder.OrderResponse;
import com.atharva.erp_telecom.entity.crm.Customer;
import com.atharva.erp_telecom.entity.salesorder.Order;
import com.atharva.erp_telecom.entity.salesorder.OrderItem;
import com.atharva.erp_telecom.entity.salesorder.Product;
import com.atharva.erp_telecom.enums.OrderStatus;
import com.atharva.erp_telecom.exception.custom_exceptions.OrderNotFoundException;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.crm.CustomerRepository;
import com.atharva.erp_telecom.repository.salesorder.OrderRepository;
import com.atharva.erp_telecom.repository.salesorder.ProductRepository;
import com.atharva.erp_telecom.service.finance.InvoiceService;
import com.atharva.erp_telecom.utils.EntityDtoMappers;
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
    private final InvoiceService invoiceService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, OrderItemService orderItemService, ProductService productService, InvoiceService invoiceService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemService = orderItemService;
        this.invoiceService = invoiceService;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderCheckoutRequest orderCheckoutRequest) {
        Long customerId = orderCheckoutRequest.getCustomerId();
        // Calling the Product repository only once to stop multiple queries i.e. the N+1 query problem.
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
        order.setOrderNumber(EntityNumberGeneratorUtil.generateOrderNumber(customer.getCustomerId()));
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
        return EntityDtoMappers.mapOrderToOrderResponse(savedOrder);
    }

    public Optional<OrderResponse> getOrderById(Long orderId) {
        return Optional.of(EntityDtoMappers.mapOrderToOrderResponse(
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new OrderNotFoundException("Order NOT FOUND for Id: "+orderId)))
        );
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(EntityDtoMappers::mapOrderToOrderResponse)
                .toList();
    }

    public String updateOrderStatus(Long orderId,OrderStatus orderStatus){
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return "Order NOT FOUND for Id: " + orderId;
        }

        Order order = orderOpt.get();
        order.setStatus(orderStatus);
        orderRepository.save(order);

        return "Order status updated to: " + orderStatus;
    }

    // Helper Method:
    private BigDecimal calculateOrderItemTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}