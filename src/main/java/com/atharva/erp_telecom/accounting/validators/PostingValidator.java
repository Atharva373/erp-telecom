package com.atharva.erp_telecom.accounting.validators;

import com.atharva.erp_telecom.accounting.engine.context.PostingContext;

public interface PostingValidator {
    boolean validate(PostingContext context);
}
