package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.AccountingEventType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

@Setter
@Getter
@ToString
public class PostingContext {
    private String sourceTransactionId;
    private String companyCode;
    private AccountingEventType eventType;
    private LocalDateTime postingDate = LocalDateTime.now();
    private String currencyCode;

    // Variables extracted from the event payload
    private Map<String, Object> variables = new HashMap<>();

    public Object getVar(String key) {
        return variables.get(key);
    }

    public void putVar(String key, Object value) {
        variables.put(key, value);
    }

}
