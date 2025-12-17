package com.atharva.erp_telecom.dto;

import com.atharva.erp_telecom.enums.AccountingEventType;

import java.time.LocalDate;
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

public class PostingContext {
    private Long sourceTransactionId;
    private Long companyId;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public AccountingEventType getEventType() {
        return eventType;
    }

    public void setEventType(AccountingEventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDateTime postingDate) {
        this.postingDate = postingDate;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Long getSourceTransactionId() {
        return sourceTransactionId;
    }

    public void setSourceTransactionId(Long sourceTransactionId) {
        this.sourceTransactionId = sourceTransactionId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
