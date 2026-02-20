package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.EntryType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostingRuleLineDTO {
    private String accountCode;  // "1100", "2100", etc.

    private EntryType entryType; // DEBIT or CREDIT

    private String amountExpression; // e.g., "event.amount", "event.amount * 0.18"

    private Integer sortOrder; // Optional ordering for deterministic execution
}
