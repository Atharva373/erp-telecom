package com.atharva.erp_telecom.testfactory;

import com.atharva.erp_telecom.accounting.domain.PostingRule;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.finance.persistence.masterdata.FinanceProfile;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static PostingContext arInvoiceContext() {
        return PostingContext.builder()
                .sourceTransactionId("INV-AR-123456")
                .companyCode("PH-MH")
                .eventType(AccountingEventType.AR_INVOICE)
                .financeProfile(new FinanceProfile())
                .financeProfileType(FinanceProfileType.AR)
                .currencyCode("INR")
                .postingDate(LocalDateTime.now())
                .build();
    }

    public static PostingRule arInvoiceRule() {
        return PostingRule.builder()
                .ruleCode("AR_STANDARD_INVOICE")
                .eventType(AccountingEventType.AR_INVOICE)
                .active(true)
                .effectiveFrom(LocalDate.now().minusDays(1))
                .lines(List.of())
                .build();
    }
}
