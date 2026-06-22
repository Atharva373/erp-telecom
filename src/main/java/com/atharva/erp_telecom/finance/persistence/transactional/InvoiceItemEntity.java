package com.atharva.erp_telecom.finance.persistence.transactional;

import com.atharva.erp_telecom.salesorder.persistence.transactional.OrderItem;
import com.atharva.erp_telecom.salesorder.persistence.masterdata.Product;
import com.atharva.erp_telecom.finance.persistence.masterdata.TaxEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_items")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class InvoiceItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceItemId;

    @Column(nullable = false, unique = true)
    private String invoiceItemNumber; // e.g., INV_10000001_ITEM_1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private InvoiceEntity invoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private TaxEntity taxEntity;

    @Column(nullable = false)
    private Integer quantity    ;

    @Column(nullable = false)
    private BigDecimal unitPrice;   // For hardware or one-off products.

    @Column(nullable = false)
    private BigDecimal baseAmount;

    @Column(nullable = false)
    private BigDecimal taxAmount;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column
    private String additionalInfo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (baseAmount == null) baseAmount = BigDecimal.ZERO;
    }

}
