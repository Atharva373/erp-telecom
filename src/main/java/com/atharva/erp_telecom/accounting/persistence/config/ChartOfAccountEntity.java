package com.atharva.erp_telecom.accounting.persistence.config;

import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import com.atharva.erp_telecom.accounting.enums.AccountSubCategory;
import com.atharva.erp_telecom.accounting.enums.NormalBalance;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "chart_of_accounts")
@EntityListeners(AuditingEntityListener.class)
public class ChartOfAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Consultant-provided or auto-generated based on number range
     *  This code can be the same for multiple companyEntity codes (legal entities) or if no companyEntity is explicitly mapped,
     *  a global masterdata can be used for the account.
     *  Example: "1110", "2200", "4100"
     */
    @Column(nullable = false, unique = true, length = 20)
    private String accountCode;

    // Example: "Bank Account", "Unearned Revenue", "Subscription Revenue"
    @Column(nullable = false, length = 100)
    private String accountName;

    // ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountCategory category;

    // Optional: BANK, RECEIVABLE, DEFERRED_REVENUE, SUBSCRIPTION_REVENUE, BREAKAGE, etc.
    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private AccountSubCategory subtype;


    /**
     * Indicates whether the natural balance for this account increases
     * via DEBIT or CREDIT.
     * Assets & Expenses --> DEBIT normal
     * Liabilities, Revenue, Equity --> CREDIT normal
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NormalBalance normalBalance;

    // General rule of thumb --> if CompanyEntity Code is null, the configuration for this accounting entity is global -- i.e. for all companyEntity codes.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity companyEntity;

    // Hierarchy (optional, supports parent-child grouping)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private ChartOfAccountEntity parentAccount;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    private LocalDate effectiveFrom = LocalDate.now();
    private LocalDate effectiveTo;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

    private String createdBy;

    private String modifiedBy;


}
