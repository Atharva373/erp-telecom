package com.atharva.erp_telecom.salesorder.api;


import com.atharva.erp_telecom.salesorder.dto.OrderCheckoutRequest;
import com.atharva.erp_telecom.salesorder.dto.OrderResponse;
import com.atharva.erp_telecom.salesorder.persistence.transactional.Order;
import com.atharva.erp_telecom.salesorder.enums.OrderStatus;
import com.atharva.erp_telecom.salesorder.persistence.repository.OrderRepository;
import com.atharva.erp_telecom.salesorder.service.OrderService;
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
            OrderResponse response = orderService.createOrder(orderCheckoutRequest);
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
        List<OrderResponse> responseList = orderService.getAllOrders();
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
        Optional<OrderResponse> optionalOrder = orderService.getOrderById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + orderId);
        }
        return ResponseEntity.ok(optionalOrder.get());
    }


    // ---------------------------
    // 🔄 UPDATE ORDER STATUS
    // ---------------------------

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        String updateStatusMessage = orderService.updateOrderStatus(orderId,status);
        return ResponseEntity.ok(updateStatusMessage);
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
