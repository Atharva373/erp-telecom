package com.atharva.erp_telecom.accounting.engine.context;

import com.atharva.erp_telecom.accounting.enums.PostingStatus;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.math.BigDecimal;

/**
*   PostingContext is the runtime container that holds all information relevant to posting a specific accounting event.

    Think of it as:

    ✔ A context object
    ✔ A data carrier
    ✔ A snapshot of the business event
    ✔ A map of all variables needed by the Posting Rule Engine

    It is passed through:

    Rule resolver

    Expression evaluator

    Condition evaluator

    Journal entry builder
*/

@Getter
@ToString
@Builder
public class PostingContext {

    private final String sourceTransactionId;
    private final String sourceSystem;
    // For future idempotency
    private final String idempotencyKey;
    private final String companyCode;
    private final AccountingEventType eventType;

    @Builder.Default
    private final LocalDateTime postingDate = LocalDateTime.now();
    private final String currencyCode;
    private final String financeProfileId;
    private final FinanceProfileType financeProfileType;
    private PostingStatus status;

    @Builder.Default
    private final Map<PostingVariableKey,Object> variables = new HashMap<>();

    public <T> T getVar(PostingVariableKey key,Class<T> clazz) {
        Object value = variables.get(key);
        if (value == null)
            return null;

        if (!clazz.isInstance(value)) {
            throw new IllegalStateException("Invalid type for key: " + key);
        }
        return clazz.cast(value);
    }

    public void putVar(PostingVariableKey key,Object value) {
        variables.put(key, value);
    }

    public void validate() {
        Objects.requireNonNull(sourceTransactionId,"sourceTransactionId cannot be null");
        Objects.requireNonNull(eventType,"eventType cannot be null");
    }
}
