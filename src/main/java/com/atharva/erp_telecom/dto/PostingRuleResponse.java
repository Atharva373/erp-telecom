package com.atharva.erp_telecom.dto;

import com.atharva.erp_telecom.enums.AccountingEventType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostingRuleResponse {
    private Long id;

    private AccountingEventType eventType;

    private String description;

    private String conditionExpression;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active;

    private List<PostingRuleLineDTO> lines;

    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private String createdBy;
    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
