package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.PeriodStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostingPeriodResponse {
    private String companyCode;
    private Integer fiscalYear;
    private Integer postingPeriod;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private PeriodStatus status;
    private LocalDateTime closedOn;
}
