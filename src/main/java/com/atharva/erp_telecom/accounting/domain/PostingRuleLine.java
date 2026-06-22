package com.atharva.erp_telecom.accounting.domain;

import com.atharva.erp_telecom.accounting.enums.EntryType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostingRuleLine {

    /**
     * Target GL account (CoA)
     * Only identity is needed in domain
     */
    private final Long accountId;

    /**
     * Debit or Credit
     */
    private final EntryType entryType;

    /**
     * Determines JE ordering
     */
    private final Integer sortOrder;

    /**
     * Optional condition for line applicability
     * Example: CUSTOMER_TYPE == 'POSTPAID'
     */
    private final String conditionExpression;

    /**
     * Amount calculation expression
     * Example: GROSS_AMOUNT, TAX_AMOUNT, NET_AMOUNT - DISCOUNT
     */
    private final String amountExpression;
}