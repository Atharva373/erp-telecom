package com.atharva.erp_telecom.accounting.engine.factory;

import com.atharva.erp_telecom.accounting.domain.event.AccountingEvent;
import com.atharva.erp_telecom.accounting.domain.event.InvoiceAccountingEvent;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.engine.context.PostingVariableKey;
import org.springframework.stereotype.Component;

@Component
public class InvoicePostingContextFactory implements PostingContextFactory{


    @Override
    public PostingContext createContext(AccountingEvent event) {
        InvoiceAccountingEvent invoice = (InvoiceAccountingEvent) event;

        PostingContext context =
                PostingContext.builder()
                        .sourceTransactionId(invoice.getSourceTransactionId())
                        .sourceSystem(invoice.getSourceSystem())
                        .companyCode(invoice.getCompanyCode())
                        .currencyCode(invoice.getCurrencyCode())
                        .eventType(invoice.getEventType())
                        .financeProfileId(invoice.getFinanceProfileId())
                        .financeProfileType(invoice.getFinanceProfileType())
                        .build();
        System.out.println(context);
        context.putVar(PostingVariableKey.AMOUNT,invoice.getAmount());
        context.putVar(PostingVariableKey.TAX_AMOUNT,invoice.getTaxAmount());
        context.putVar(PostingVariableKey.INVOICE_ID,invoice.getInvoiceId());
        return context;
    }

    @Override
    public Class<? extends AccountingEvent> supports() {
        return InvoiceAccountingEvent.class;
    }
}
