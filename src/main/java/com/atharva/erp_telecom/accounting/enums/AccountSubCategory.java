package com.atharva.erp_telecom.accounting.enums;

public enum AccountSubCategory {
    // Asset subtypes
    CASH,
    BANK,
    ACCOUNTS_RECEIVABLE,
    PREPAID_ASSET,

    // Liability subtypes
    DEFERRED_REVENUE,
    ACCOUNTS_PAYABLE,
    CUSTOMER_DEPOSIT,

    // Revenue subtypes
    SUBSCRIPTION_REVENUE,
    USAGE_REVENUE,
    ONE_TIME_REVENUE,
    BREAKAGE_REVENUE,
    PENALTY_AND_LATE_FEE,
    DISCOUNTS_AND_PROMOTIONS,

    // Expense subtypes
    NETWORK_COST,
    PAYMENT_GATEWAY_FEES,
    CUSTOMER_SUPPORT,
    DEPRECIATION,
    REFUND_EXPENSE,
    BAD_DEBT_EXPENSE,

    // TaxEntity or misc. categories
    TAX,
    MISC

}
