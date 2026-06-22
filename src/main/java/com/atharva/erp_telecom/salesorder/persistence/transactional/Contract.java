package com.atharva.erp_telecom.salesorder.persistence.transactional;


import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.invoicing.persistence.masterdata.ChargePlan;
import com.atharva.erp_telecom.salesorder.enums.ContractStatus;
import com.atharva.erp_telecom.salesorder.persistence.masterdata.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Contract {

    public Contract() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @Column(nullable = false, unique = true, updatable = false)
    private String contractNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_entity_id",nullable = false)
    private BusinessEntity businessEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column
    private LocalDate contractStartDate;

    @Column
    private LocalDate contractEndDate;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ContractStatus contractStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    // NOTE: @JsonBackReference is used for mapping back to the Parent entity. This annotation is mostly used in the Child entity to prevent infinite recursion.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_agreement_id")
    @JsonBackReference
    private MasterAgreement masterAgreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_plan_id")
    private ChargePlan chargePlan;

    @Column(length = 255)
    private String deactivationReason;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime updatedOn;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @PrePersist
    public void onCreate() {
        if (this.contractStatus == null) {
            this.contractStatus = ContractStatus.INACTIVE;
        }
    }


}