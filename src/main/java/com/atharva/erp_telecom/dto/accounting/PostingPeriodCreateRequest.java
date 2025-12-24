package com.atharva.erp_telecom.dto.accounting;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostingPeriodCreateRequest {

    private Long companyId;
    private Integer fiscalYear;
    private Integer postingPeriod;
    private LocalDate periodStart;
    private LocalDate periodEnd;

}
