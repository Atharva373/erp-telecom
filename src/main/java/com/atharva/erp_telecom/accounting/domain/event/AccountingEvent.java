package com.atharva.erp_telecom.accounting.domain.event;

import com.atharva.erp_telecom.accounting.enums.AccountingEventType;

public interface AccountingEvent {
    String getSourceTransactionId();

    String getCompanyCode();

    String getCurrencyCode();

    AccountingEventType getEventType();
}
