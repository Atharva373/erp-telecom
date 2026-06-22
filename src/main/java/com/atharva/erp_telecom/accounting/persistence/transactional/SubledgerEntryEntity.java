package com.atharva.erp_telecom.accounting.persistence.transactional;

import com.atharva.erp_telecom.accounting.persistence.config.SubledgerAccountEntity;
import com.atharva.erp_telecom.accounting.enums.SubledgerEventType;
import com.atharva.erp_telecom.accounting.enums.SubledgerStatus;
import com.atharva.erp_telecom.accounting.enums.SubledgerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "subledger_entry",
        indexes = {
                @Index(name = "idx_subledger_entry_open", columnList = "company_code,subledger_type,status"),
                @Index(name = "idx_subledger_entry_due", columnList = "company_code,due_date"),
                @Index(name = "idx_subledger_entry_gl", columnList = "gl_journal_entry_id")
        }
)
@Getter
@Setter
public class SubledgerEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subledger_account_id", nullable = false)
    private SubledgerAccountEntity subledgerAccountEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "subledger_type", nullable = false)
    private SubledgerType subledgerType; // AR / AP

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SubledgerEventType eventType;

    @Column(name = "business_ref_type", nullable = false)
    private String businessRefType; // INVOICE, PAYMENT, etc.

    @Column(name = "business_ref_id", nullable = false)
    private Long businessRefId;

    /**
     * Reference to original subledger entry (for reversals, credit memos, refunds)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_subledger_entry_id")
    private SubledgerEntryEntity relatedSubledgerEntryEntity;

    @Column(name = "posting_date", nullable = false)
    private LocalDate postingDate;

    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "original_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "open_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal openAmount;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubledgerStatus status;

    /**
     * Strong GL ↔ Subledger linkage (replaces SAP reconciliation keys)
     */
    @Column(name = "gl_journal_entry_id", nullable = false)
    private Long glJournalEntryId;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();
}
