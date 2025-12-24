package com.atharva.erp_telecom.entity;

import com.atharva.erp_telecom.enums.PeriodStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "posting_periods",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_company_year_period",
                        columnNames = {"company_id", "fiscal_year", "posting_period"}
                )
        }
)
@Data
@EntityListeners(AuditingEntityListener.class)
public class PostingPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Company this period belongs to */
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    /** Fiscal year (e.g. 2025) */
    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    /**
     * Posting period:
     * 1–12 (calendar months)
     * 13 reserved for future adjustment period
     */
    @Column(name = "posting_period", nullable = false)
    private Integer postingPeriod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodStatus status;

    /** Actual calendar range */
    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    /** Audit fields for closing */
    private LocalDateTime closedOn;
    private String closedBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;
}
