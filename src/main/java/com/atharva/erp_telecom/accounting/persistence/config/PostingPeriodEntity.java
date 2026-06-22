package com.atharva.erp_telecom.accounting.persistence.config;

import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class PostingPeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** CompanyEntity this period belongs to */
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "company_id",nullable = false)
    private CompanyEntity company;

    /** Fiscal year (e.g. 2026) */
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
    private LocalDateTime periodStart;

    @Column(nullable = false)
    private LocalDateTime periodEnd;

    /** Audit fields for closing */
    private LocalDateTime closedOn;
    private String closedBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;
}
