package com.atharva.erp_telecom.accounting.domain;


import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
public class PostingRule {

    private final String ruleCode;
    private final AccountingEventType eventType;
    private final Long companyId; // nullable = global rule
    private final List<PostingRuleLine> lines;
    private final boolean active;
    private final LocalDate effectiveFrom;
    private final LocalDate effectiveTo;
    private final String headerConditionExpression;



    public PostingRule(
            String ruleCode,
            AccountingEventType eventType,
            Long companyId,
            List<PostingRuleLine> lines,
            boolean active,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            String headerConditionExpression
    ) {
        this.ruleCode = ruleCode;
        this.eventType = eventType;
        this.companyId = companyId;
        this.lines = List.copyOf(lines);
        this.active = active;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.headerConditionExpression = headerConditionExpression;
    }



    public boolean isActiveOn(LocalDate date) {
        if (!active) return false;
        if (date.isBefore(effectiveFrom)) return false;
        return effectiveTo == null || !date.isAfter(effectiveTo);
    }

    public boolean isGlobal() {
        return companyId == null;
    }

    public boolean matchesCompany(Long companyId) {
        return isGlobal() || Objects.equals(this.companyId, companyId);
    }

}
