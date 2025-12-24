package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.EntryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * READ - ONLY VIEW for viewing all GL entries filtered by a criterion.
 *
 * General Ledger answers:
 * “Show me every transaction that affected this account.”
 * Example (Cash account):
 *  --
 * Date	JE ID	Description	Debit	Credit	Balance
 * 01-Mar	JE-101	Invoice	10,000		10,000
 * 05-Mar	JE-102	Payment		5,000	5,000
 *  --
 * GL is:
 *  chronological
 *  detailed
 *  traceable
 */

@Data
public class GLLineRow {

    private LocalDate postingDate;
    private Long journalEntryId;

    private EntryType entryType;
    private BigDecimal amount;

    // Calculated in service
    private BigDecimal runningBalance;

    private String sourceTransactionType;
    private Long sourceTransactionId;

}

