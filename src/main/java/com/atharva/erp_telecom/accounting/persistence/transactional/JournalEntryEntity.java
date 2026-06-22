package com.atharva.erp_telecom.accounting.persistence.transactional;

import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
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

@Setter
@Getter
@Entity
@Table(name = "journal_entries")
@EntityListeners(AuditingEntityListener.class)
public class JournalEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountingEventType eventType;

    @Column(nullable = false)
    private String companyCode;

    /** Reference to rule used */
    @Column(nullable = false)
    private Long postingRuleId;

    @Column(nullable = false)
    private String ledger = "PRIMARY";

    /** External event reference (InvoiceEntity ID, Payment ID, etc.) */
    private String sourceTransactionId;

    /** Allows traceability for batch runs, integrations */
    private String sourceSystem;

    // DR & CR totals for quick financial validation
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDebit;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCredit;

    @Column(nullable = false)
    private LocalDateTime postingDate;

    @OneToMany(mappedBy = "journalEntryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalEntryLineEntity> lines = new ArrayList<>();

    @Column(nullable = false,length = 3)
    private String currencyCode;

    // Auto-calculated, validated: SUM of all DEBIT lines == SUM of all CREDIT lines
    private boolean balanced;

    private boolean posted = true;   // In some ERPs, JE is draft until posted
    private boolean reversed = false;

    @Column(name = "reversal_of")
    private Long reversalOf; // ID of JE it reverses

    private Integer fiscalYear;
    private Integer postingPeriod;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

    private String createdBy;
    private String modifiedBy;

}
