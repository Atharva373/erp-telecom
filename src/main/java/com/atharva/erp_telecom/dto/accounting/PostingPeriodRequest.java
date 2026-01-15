package com.atharva.erp_telecom.dto.accounting;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PostingPeriodRequest {
    private String companyCode;
    private Integer fiscalYear;
    private Integer postingPeriod;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
}
