package com.atharva.erp_telecom.entity.accounting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "subledger_clearing",
        indexes = {
                @Index(name = "idx_subledger_clearing_debit", columnList = "debit_entry_id"),
                @Index(name = "idx_subledger_clearing_credit", columnList = "credit_entry_id")
        }
)
@Getter
@Setter
public class SubledgerClearing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debit_entry_id", nullable = false)
    private SubledgerEntry debitEntry; // Invoice / Debit

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_entry_id", nullable = false)
    private SubledgerEntry creditEntry; // Payment / Credit Memo

    @Column(name = "cleared_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal clearedAmount;

    @Column(name = "clearing_date", nullable = false)
    private LocalDate clearingDate;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();
}
