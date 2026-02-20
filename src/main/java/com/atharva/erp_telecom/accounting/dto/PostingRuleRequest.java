package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PostingRuleRequest {

    private String postingRuleCode;

    private AccountingEventType eventType;

    private String description;

    private String headerConditionExpression;   // OPTIONAL: "paymentType == 'PREPAID'"

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active = true;

    private String companyCode;

    private List<PostingRuleLineDTO> lines;

}
