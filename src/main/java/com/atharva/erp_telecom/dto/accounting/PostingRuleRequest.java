package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.AccountingEventType;

import java.time.LocalDate;
import java.util.List;

public class PostingRuleRequest {
    private AccountingEventType eventType;

    private String description;

    private String conditionExpression;   // OPTIONAL: "paymentType == 'PREPAID'"

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active = true;

    private String companyCode;

    private List<PostingRuleLineDTO> lines;

    public AccountingEventType getEventType() {
        return eventType;
    }

    public void setEventType(AccountingEventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<PostingRuleLineDTO> getLines() {
        return lines;
    }

    public void setLines(List<PostingRuleLineDTO> lines) {
        this.lines = lines;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
