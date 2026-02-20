package com.atharva.erp_telecom.testfactory;

import com.atharva.erp_telecom.accounting.dto.PostingContext;
import com.atharva.erp_telecom.finance.persistence.masterdata.FinanceProfile;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static PostingContext arInvoiceContext() {
        FinanceProfile fp = new FinanceProfile();
        fp.setProfileCode("AR-TEST");
        fp.setProfileType(FinanceProfileType.AR);

        return new PostingContext();
    }
}
