package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TrialBalanceRow {

    private String accountCode;
    private String accountName;
    private AccountCategory accountCategory;

    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    // Convenience
    public BigDecimal getBalance() {
        return totalDebit.subtract(totalCredit);
    }

}
