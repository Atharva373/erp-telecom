package com.atharva.erp_telecom.enums;

public enum AccountSubtype {
    // Asset subtypes
    CASH,
    BANK,
    ACCOUNTS_RECEIVABLE,
    PREPAID_ASSET,

    // Liability subtypes
    UNEARNED_REVENUE,
    ACCOUNTS_PAYABLE,
    CUSTOMER_DEPOSIT,

    // Revenue subtypes
    SUBSCRIPTION_REVENUE,
    USAGE_REVENUE,
    ONE_TIME_REVENUE,
    BREAKAGE_REVENUE,

    // Expense subtypes
    NETWORK_COST,
    PAYMENT_GATEWAY_FEES,
    CUSTOMER_SUPPORT,
    DEPRECIATION,
    REFUND_EXPENSE,

    // Tax or misc. categories
    TAX,
    MISC
}
