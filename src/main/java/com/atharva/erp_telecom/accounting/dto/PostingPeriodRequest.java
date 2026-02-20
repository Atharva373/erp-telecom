package com.atharva.erp_telecom.accounting.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
