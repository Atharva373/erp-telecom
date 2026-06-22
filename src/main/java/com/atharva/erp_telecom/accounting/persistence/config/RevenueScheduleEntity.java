package com.atharva.erp_telecom.accounting.persistence.config;

import com.atharva.erp_telecom.accounting.enums.RevenueRecognitionType;
import com.atharva.erp_telecom.accounting.enums.RevenueScheduleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "revenue_schedules",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sourceType","sourceId"}
        ))
@Getter
@Setter
public class RevenueScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Business source */
    private String sourceType;     // SUBSCRIPTION, USAGE, CONTRACT
    private Long sourceId;

    /* CompanyEntity */
    private String companyCode;

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
