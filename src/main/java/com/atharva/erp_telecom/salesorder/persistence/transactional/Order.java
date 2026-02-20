package com.atharva.erp_telecom.salesorder.persistence.transactional;

import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceEntity;
import com.atharva.erp_telecom.salesorder.enums.OrderStatus;
import com.atharva.erp_telecom.salesorder.enums.OrderType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false, unique = true, updatable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_entity_id", nullable = false)
    private BusinessEntity businessEntity;

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

    // Removed the CascadeType.ALL to mitigate 2 database inserts - one in InvoiceEntity and one in Order.
    @OneToOne(mappedBy = "order",  fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private InvoiceEntity invoiceEntity;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime updatedOn;

    // Utility method for adding items safely to and forth.
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }


    public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
        this.invoiceEntity = invoiceEntity;
        if(invoiceEntity !=null && invoiceEntity.getOrder() != this){
            invoiceEntity.setOrder(this);
        }
    }

}
