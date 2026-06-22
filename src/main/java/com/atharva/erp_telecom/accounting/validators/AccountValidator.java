package com.atharva.erp_telecom.accounting.validators;

import com.atharva.erp_telecom.accounting.engine.context.PostingContext;

public class AccountValidator implements PostingValidator{
    @Override
    public boolean validate(PostingContext context) {
        return false;
    }
}
