package com.atharva.erp_telecom.accounting.api.simulation;

import com.atharva.erp_telecom.accounting.domain.event.InvoiceAccountingEvent;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.accounting.service.AccountingService;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("accounting/simulate")
public class AccountingSimulationController {

    private final AccountingService accountingService;

    @PostMapping("/invoice")
    public JournalEntryEntity simulateInvoicePosting() {
        InvoiceAccountingEvent event = InvoiceAccountingEvent
                        .builder()
                        .sourceTransactionId("TXN-1001")
                        .invoiceId("INV-1001")
                        .companyCode("PHOTON_MH")
                        .currencyCode("INR")
                        .eventType(AccountingEventType.AR_INVOICE)
                        .financeProfileId("FP001")
                        .financeProfileType(FinanceProfileType.AR)
                        .amount(BigDecimal.valueOf(1000))
                        .taxAmount(BigDecimal.valueOf(180))
                        .build();
        System.out.println(event);
        return accountingService.simulate(event);
    }
}