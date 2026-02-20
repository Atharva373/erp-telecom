package com.atharva.erp_telecom.accounting.persistence.transactional;

import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import com.atharva.erp_telecom.accounting.enums.AccountSubCategory;
import com.atharva.erp_telecom.accounting.enums.EntryType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "journal_entry_lines")
@EntityListeners(AuditingEntityListener.class)
public class JournalEntryLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Parent FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    @JsonBackReference
    private JournalEntryEntity journalEntryEntity;

    /** Which account was impacted */
    @Column(nullable = false)
    private String accountCode;

    /** Debit or Credit */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType;

    /** Always stored as POSITIVE value */
    @Column(nullable = false)
    private BigDecimal amount;

    // Denormalized metadata (faster reporting)
    private String accountName;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Enumerated(EnumType.STRING)
    private AccountCategory accountCategory;

    @Enumerated(EnumType.STRING)
    private AccountSubCategory accountSubCategory;

    // For audit
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

    private String createdBy;
    private String modifiedBy;


}
