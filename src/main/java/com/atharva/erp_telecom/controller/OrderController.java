package com.atharva.erp_telecom.controller;


import com.atharva.erp_telecom.dto.OrderCheckoutRequest;
import com.atharva.erp_telecom.dto.OrderResponse;
import com.atharva.erp_telecom.entity.Order;
import com.atharva.erp_telecom.enums.OrderStatus;
import com.atharva.erp_telecom.repository.OrderRepository;
import com.atharva.erp_telecom.service.OrderService;
import com.atharva.erp_telecom.utils.EntityDtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Autowired

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    // ---------------------------
    // 🛒 CREATE / CHECKOUT ORDER
    // ---------------------------
    @PostMapping("/checkout")
    public ResponseEntity<?> createOrder(@RequestBody OrderCheckoutRequest orderCheckoutRequest) {
        try {
            Order order = orderService.createOrder(orderCheckoutRequest);
            OrderResponse response = EntityDtoMappers.mapOrderToOrderResponse(order);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating order: " + e.getMessage());
        }
    }


    // ---------------------------
    // 📄 GET ALL ORDERS
    // ---------------------------
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> responseList = orders.stream()
                .map(EntityDtoMappers::mapOrderToOrderResponse)
                .toList();
        if (responseList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseList);
    }

    // ---------------------------
    // 🔍 GET ORDER BY ID
    // ---------------------------
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        Optional<Order> optionalOrder = orderService.getOrder(orderId);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + orderId);
        }

        OrderResponse response = EntityDtoMappers.mapOrderToOrderResponse(optionalOrder.get());
        return ResponseEntity.ok(response);
    }


    // ---------------------------
    // 🔄 UPDATE ORDER STATUS
    // ---------------------------

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + orderId);
        }

        Order order = orderOpt.get();
        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok("Order status updated to: " + status);
    }

    // ---------------------------
    // 🗑 DELETE ORDER
    // ---------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + orderId);
        }

        orderRepository.delete(orderOpt.get());
        return ResponseEntity.ok("Order deleted successfully.");
    }

}
