package com.atharva.erp_telecom.accounting.domain.event;

import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.finance.persistence.masterdata.FinanceProfile;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class InvoiceAccountingEvent implements AccountingEvent{

    private final String sourceTransactionId;
    private final String sourceSystem;
    private final String companyCode;
    private final String currencyCode;

    private final AccountingEventType eventType;

    private final String invoiceId;
    private final String financeProfileId;
    private final FinanceProfileType financeProfileType;
    private final BigDecimal amount;
    private final BigDecimal taxAmount;

    @Override
    public String getSourceTransactionId() {
        return this.sourceTransactionId;
    }

    @Override
    public String getCompanyCode() {
        return this.companyCode;
    }

    @Override
    public String getCurrencyCode() {
        return this.currencyCode;
    }

    @Override
    public AccountingEventType getEventType() {
        return this.eventType;
    }
}
