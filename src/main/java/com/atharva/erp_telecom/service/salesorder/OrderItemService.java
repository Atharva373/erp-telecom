package com.atharva.erp_telecom.service.salesorder;

import com.atharva.erp_telecom.dto.salesorder.OrderCheckoutRequest;
import com.atharva.erp_telecom.entity.charging.ChargePlan;
import com.atharva.erp_telecom.entity.salesorder.Order;
import com.atharva.erp_telecom.entity.salesorder.OrderItem;
import com.atharva.erp_telecom.entity.salesorder.Product;
import com.atharva.erp_telecom.enums.PlanType;
import com.atharva.erp_telecom.exception.custom_exceptions.ChargePlanNotFoundException;
import com.atharva.erp_telecom.exception.custom_exceptions.ProductNotFoundException;
import com.atharva.erp_telecom.repository.salesorder.OrderItemRepository;
import com.atharva.erp_telecom.service.salesorder.implementation.ProductServiceImplementation;
import com.atharva.erp_telecom.utils.GenericUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductServiceImplementation productService;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, ProductServiceImplementation productService) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    @Transactional
    public List<OrderItem> createOrderItems(Order order, OrderCheckoutRequest orderCheckoutRequest, Map<Long,Product> productMap) {
        if (order == null  || orderCheckoutRequest == null || productMap.isEmpty()) {
            throw new IllegalArgumentException("Order or Products cannot be null or empty.");
        }
        AtomicInteger itemCounter = new AtomicInteger(1);

        return orderCheckoutRequest.getOrderProducts().stream().map(orderProduct -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            if(orderProduct.getProductId() == null){
                throw new ProductNotFoundException("Product Id is null, can't fetch product...");
            }
            Product fetchedProduct = productMap.get(orderProduct.getProductId());
            BigDecimal oneOffPrice = GenericUtils.getDefaultChargePlan(fetchedProduct).getOneOffCharge();
            item.setProduct(fetchedProduct);
            item.setQuantity(orderProduct.getQuantity());
            item.setPrice(determineProductPriceForOrderItem(fetchedProduct));
            item.setUnitPrice(oneOffPrice);
            String orderLineItemNumber = String.format(
                    "%s_ITEM_%d", order.getOrderNumber(), itemCounter.getAndIncrement());
            item.setOrderLineItemNumber(orderLineItemNumber);
            item.setChargePlanType(determineChargePlanType(fetchedProduct));
            item.setCreatedOn(LocalDateTime.now());
            item.setUpdatedOn(LocalDateTime.now());
            // Contract will be created later, currently populated as null.
            order.addItem(item);
            item.setCreatedBy("PHOTON_ERP");
            item.setUpdatedBy("PHOTON_ERP");
            return item;

        }).collect(Collectors.toList());
    }

    /**
     * Get an OrderItem by Id
     */

    @Transactional
    public OrderItem getOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found with ID: " + orderItemId));
    }

    /**
     * Delete an order item.
     */
    @Transactional
    public void deleteOrderItem(Long orderItemId) {
        OrderItem item = getOrderItem(orderItemId);
        orderItemRepository.delete(item);
    }

    // -------------------
    // Helper Methods
    // -------------------

    /**
     * Determines product price.
     * (This can be extended to pull from rate cards, charge plans, etc.)
     */
    private BigDecimal determineProductPriceForOrderItem(Product product) {
        return productService.getDisplayOnlyProductPrice(product);
    }

    /**
     * Determines ChargePlan type. (PREPAID / POSTPAID)
     */
    private PlanType determineChargePlanType(Product product) {
        if (product.getChargePlans() == null || product.getChargePlans().isEmpty()) {
            throw new ChargePlanNotFoundException("ChargePlan for Product ID: "+ product.getProductId() + " does not exist.");
        }

        var defaultPlans = product.getChargePlans().stream()
                .filter(ChargePlan::isDefault)
                .toList();

        if (defaultPlans.isEmpty()) {
            throw new ChargePlanNotFoundException("No default ChargePlan found for Product ID: " + product.getProductId());
        }

        var planTypes = defaultPlans.stream()
                .map(ChargePlan::getPlanType)
                .collect(Collectors.toSet());

        if (planTypes.size() > 1) {
            throw new IllegalStateException(
                    "Product " + product.getProductId() + " has multiple charge plan types (mixed prepaid/postpaid).");
        }

        return planTypes.iterator().next();
    }
}
