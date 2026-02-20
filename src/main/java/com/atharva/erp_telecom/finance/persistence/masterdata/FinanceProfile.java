package com.atharva.erp_telecom.finance.persistence.masterdata;


import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_profile")
@EntityListeners(AuditingEntityListener.class)
@Data
public class FinanceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long financeProfileId;

    /* ---------- Ownership ---------- */

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_entity_id", nullable = false)
    private BusinessEntity businessEntity;

    /* ---------- Nature ---------- */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FinanceProfileType profileType; // AR / AP

    /**
     * Human-friendly identifier
     * Example: AR-ENT-INR-001
     */
    @Column(nullable = false, unique = true)
    private String profileCode;

    /* ---------- Accounting Controls ---------- */

    /**
     * Control account used for subledger posting
     * (e.g. Accounts Receivable)
     */
    @Column(nullable = false)
    private Long controlAccountId;

    private String currencyCode; // INR, USD

    /* ---------- Commercial Terms ---------- */

    private Integer paymentTermDays; // Net 30, Net 45

    private BigDecimal creditLimit;

    private Boolean creditCheckRequired = Boolean.FALSE;

    /* ---------- Status ---------- */

    private Boolean active = Boolean.TRUE;

    /* ---------- Audit ---------- */

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedOn;
}

