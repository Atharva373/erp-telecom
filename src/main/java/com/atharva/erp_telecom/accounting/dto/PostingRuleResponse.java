package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostingRuleResponse {
    private Long id;

    private String postingRuleCode;

    private AccountingEventType eventType;

    private String description;

    private String headerConditionExpression;

    private LocalDate effectiveFrom;

    private String companyCode;

    private LocalDate effectiveTo;

    private Boolean active;

    private List<PostingRuleLineDTO> lines;

    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private String createdBy;
    private String modifiedBy;

}
