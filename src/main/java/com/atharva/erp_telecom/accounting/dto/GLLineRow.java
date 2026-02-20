package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.accounting.enums.EntryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * READ - ONLY VIEW for viewing all GL entries filtered by a criterion.
 *
 * General Ledger answers:
 * “Show me every transaction that affected this account.”
 * Example (Cash account):
 *  --
 * Date	JE ID	Description	Debit	Credit	Balance
 * 01-Mar	JE-101	InvoiceEntity	10,000		10,000
 * 05-Mar	JE-102	Payment		5,000	5,000
 *  --
 * GL is:
 *  chronological
 *  detailed
 *  traceable
 */

@Getter
@Setter
public class GLLineRow {

    LocalDateTime postingDate;
    private Long journalEntryId;

    private EntryType entryType;
    private BigDecimal amount;

    // Calculated in service
    private BigDecimal runningBalance;

    private AccountingEventType sourceTransactionType;
    private Long sourceTransactionId;

    // Constructors for JPQL Queries
    public GLLineRow(LocalDateTime postingDate, Long id, EntryType entryType, BigDecimal amount, AccountingEventType eventType, Long sourceTransactionId)  {
        this.postingDate = postingDate;
        this.journalEntryId = id;
        this.entryType = entryType;
        this.amount = amount;
        this.sourceTransactionType = eventType;
        this.sourceTransactionId = sourceTransactionId;
    }

//    // Constructors for general object creation
//    public GLLineRow(LocalDateTime postingDate, Long journalEntryId, EntryType entryType, BigDecimal amount, BigDecimal runningBalance, AccountingEventType sourceTransactionType, Long sourceTransactionId) {
//        this.postingDate = postingDate;
//        this.journalEntryId = journalEntryId;
//        this.entryType = entryType;
//        this.amount = amount;
//        this.runningBalance = runningBalance;
//        this.sourceTransactionType = sourceTransactionType;
//        this.sourceTransactionId = sourceTransactionId;
//    }
}

