package com.atharva.erp_telecom.enums;

public enum AccountingEventType {
        // 1. Payments
        PAYMENT_RECEIVED_PREPAID,
        PAYMENT_RECEIVED_POSTPAID,
        PAYMENT_REVERSED,
        REFUND_PROCESSED,
        SECURITY_DEPOSIT_RECEIVED,

        // 2. Billing & Revenue
        INVOICE_GENERATED,
        INVOICE_CANCELLED,
        REVENUE_RECOGNIZED_SUBSCRIPTION,
        REVENUE_RECOGNIZED_USAGE,
        REVENUE_RECOGNIZED_ONE_TIME,
        REVENUE_REVERSAL,

        // 3. Usage & Rating
        USAGE_RATED_PREPAID,
        USAGE_RATED_POSTPAID,
        UNBILLED_REVENUE_ACCRUED,
        UNBILLED_REVENUE_REVERSED,

        // 4. Adjustments
        CREDIT_NOTE_ISSUED,
        DEBIT_NOTE_ISSUED,
        DISCOUNT_APPLIED,
        WAIVER_GRANTED,
        BAD_DEBT_WRITTEN_OFF,
        LATE_FEE_APPLIED,
        DUNNING_FEE_APPLIED,

        // 5. Taxes
        TAX_RECORDED,
        TAX_ADJUSTED,
        TAX_PAID_TO_AUTHORITY,

        // 6. Wallet & Breakage
        WALLET_EXPIRED_BREAKAGE,
        WALLET_TOPUP_ADJUSTED,

        // 7. Asset / Hardware
        INVENTORY_SOLD,
        ASSET_CAPITALIZED,
        ASSET_DEPRECIATED,

        // 8. Period Events
        PERIOD_ACCRUAL_POSTED,
        PERIOD_ACCRUAL_REVERSED,
    REVREC_DEFERRED, REVREC_ACCRUED, PERIOD_CLOSED

}
