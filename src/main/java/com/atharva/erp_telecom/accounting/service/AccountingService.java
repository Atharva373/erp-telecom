package com.atharva.erp_telecom.accounting.service;


import com.atharva.erp_telecom.accounting.domain.event.AccountingEvent;
import com.atharva.erp_telecom.accounting.engine.PostingEngine;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.engine.factory.PostingContextFactory;
import com.atharva.erp_telecom.accounting.engine.factory.PostingContextFactoryResolver;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@ToString
public class AccountingService {

    private final PostingContextFactoryResolver factoryResolver;
    private final PostingEngine postingEngine;

    public JournalEntryEntity process(AccountingEvent event) {
        log.info(
                "Received accounting event type={}, sourceTxId={}",
                event.getEventType(),
                event.getSourceTransactionId()
        );
        PostingContextFactory factory = factoryResolver.resolve(event);
        PostingContext context = factory.createContext(event);
        context.validate();
        return postingEngine.post(context);
    }


    public JournalEntryEntity simulate(AccountingEvent event) {
        PostingContextFactory factory = factoryResolver.resolve(event);
        PostingContext context = factory.createContext(event);
        context.validate();
        return postingEngine.simulate(context);
    }
}
