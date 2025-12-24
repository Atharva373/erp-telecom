package com.atharva.erp_telecom.dto.accounting;

import lombok.Data;

import java.math.BigDecimal;

/**
 * READ - ONLY VIEW for viewing all Trial balances filtered by a criterion.
 *
 * Trial Balance answers:
 * “Do debits equal credits, and what is the balance of each account?”
 * A trial balance is essentially:
 * Account → Total Debit → Total Credit → Balance
 * --
 * Trial Balance is:
 *  summarized
 *  aggregated
 *  diagnostic
 */

@Data
public class TrialBalanceRow {

    private String accountCode;
    private String accountName;
    private String accountCategory;

    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    // Convenience
    public BigDecimal getBalance() {
        return totalDebit.subtract(totalCredit);
    }

}
