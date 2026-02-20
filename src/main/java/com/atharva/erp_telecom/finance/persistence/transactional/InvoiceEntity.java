package com.atharva.erp_telecom.finance.persistence.transactional;

import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.salesorder.persistence.transactional.Order;
import com.atharva.erp_telecom.invoicing.enums.InvoiceStatus;
import com.atharva.erp_telecom.invoicing.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_entity_id", nullable = false)
    private BusinessEntity businessEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemEntity> invoiceItemEntities = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal taxTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private InvoiceStatus status; // e.g., CREATED, PAID, CANCELLED

    @Column(length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentStatus paymentStatus;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @Column(length = 100, updatable = false)
    private String createdBy;

    @Column(length = 100)
    private String modifiedBy;

    // --- Helper Methods ---
    public void addItem(InvoiceItemEntity item) {
        invoiceItemEntities.add(item);
        item.setInvoice(this);
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null && order.getInvoiceEntity() != this) {
            order.setInvoiceEntity(this);
        }
    }

}
