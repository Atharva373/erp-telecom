package com.atharva.erp_telecom.accounting.engine.factory;


import com.atharva.erp_telecom.accounting.domain.event.AccountingEvent;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;

public interface PostingContextFactory{
    PostingContext createContext(AccountingEvent event);
    // Using Class<> wrapper here to determine the concrete type at runtime
    Class<? extends AccountingEvent> supports();
}
