package com.atharva.erp_telecom.entity.salesorder;

import com.atharva.erp_telecom.entity.finance.Invoice;
import com.atharva.erp_telecom.entity.crm.Customer;
import com.atharva.erp_telecom.enums.OrderStatus;
import com.atharva.erp_telecom.enums.OrderType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false, unique = true, updatable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // PREPAID or POSTPAID

    /*
        NOTE: Very,very important -->
        1. The owning side of the relationship should contain the @JoinColumn annotation not the non-owning side.
        2. The non-owning side should contain the @XToY(mappedBy=..) property as a good practice which suggests that
            foreign key (FK) relation is already been established already.

            In simple words, it literally means:

            “This side is NOT the owner of the relationship.
            The other side already has the foreign key column.
            Use that one — not this one.”

        3. Table:

            | Relationship Type | Owning Side    | Annotation                   | Non-owning Side | Annotation             |
            | ----------------- | -------------- | ---------------------------- | --------------- | ---------------------- |
            | `@OneToMany`      | Child          | `@ManyToOne @JoinColumn`     | Parent          | `@OneToMany(mappedBy)` |
            | `@OneToOne`       | Entity with FK | `@JoinColumn`                | Other side      | `@OneToOne(mappedBy)`  |
            | `@ManyToMany`     | Chosen owner   | `@JoinTable` / `@JoinColumn` | Other side      | `mappedBy`             |

        4. Ideally the non-owning entity should reference back in the service layer.
            e.g. child.setParent(parent);

     */

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column
    private String remarks;

    // Removed the CascadeType.ALL to mitigate 2 database inserts - one in Invoice and one in Order.
    @OneToOne(mappedBy = "order",  fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private Invoice invoice;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime updatedOn;

    // Logic moved to Service and Util classes.
//    @PrePersist
//    public void onCreate() {
//        if (this.orderNumber == null && this.customer != null) {
//            this.orderNumber = String.format(
//                    "ORD_%s_%s",
//                    customer.getCustomerId(),
//                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
//            );
//        }
//    }

    // Utility method for adding items safely to and forth.
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        if(invoice!=null && invoice.getOrder() != this){
            invoice.setOrder(this);
        }
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
