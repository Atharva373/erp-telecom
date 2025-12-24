package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.EntryType;

public class PostingRuleLineDTO {
    private String accountCode;  // "1100", "2100", etc.

    private EntryType entryType; // DEBIT or CREDIT

    private String amountExpression; // e.g., "event.amount", "event.amount * 0.18"

    private Integer sequenceOrder; // Optional ordering for deterministic execution

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public String getAmountExpression() {
        return amountExpression;
    }

    public void setAmountExpression(String amountExpression) {
        this.amountExpression = amountExpression;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
}
