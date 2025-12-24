package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.RevenueRecognitionType;
import com.atharva.erp_telecom.enums.RevenueScheduleStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class RevenueRecognitionJobRunStatusDTO {

    private Long scheduleId;
    private String sourceType;
    private Long sourceId;

    private RevenueRecognitionType recognitionType;

    private BigDecimal totalAmount;
    private BigDecimal recognizedAmount;
    private BigDecimal remainingAmount;

    private YearMonth lastRecognizedPeriod;
    private RevenueScheduleStatus status;
}


