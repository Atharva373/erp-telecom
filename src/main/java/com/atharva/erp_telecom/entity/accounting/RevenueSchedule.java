package com.atharva.erp_telecom.entity.accounting;

import com.atharva.erp_telecom.enums.RevenueRecognitionType;
import com.atharva.erp_telecom.enums.RevenueScheduleStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "revenue_schedules",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sourceType","sourceId"}
        ))
@Data
public class RevenueSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Business source */
    private String sourceType;     // SUBSCRIPTION, USAGE, CONTRACT
    private Long sourceId;

    /* Company */
    private Long companyId;

    /* Recognition behavior */
    @Enumerated(EnumType.STRING)
    private RevenueRecognitionType recognitionType;
    // DEFERRED | ACCRUED

    /* Amounts */
    private BigDecimal totalAmount;
    private BigDecimal recognizedAmount = BigDecimal.ZERO;
    private BigDecimal remainingAmount;

    /* Time window */
    private LocalDate startDate;
    private LocalDate endDate;

    /* Idempotency */
    private YearMonth lastRecognizedPeriod;

    @Enumerated(EnumType.STRING)
    private RevenueScheduleStatus status;
}
