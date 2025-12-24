package com.atharva.erp_telecom.entity.accounting;

import com.atharva.erp_telecom.entity.salesorder.Company;
import com.atharva.erp_telecom.enums.AccountCategory;
import com.atharva.erp_telecom.enums.AccountSubtype;
import com.atharva.erp_telecom.enums.NormalBalance;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "chart_of_accounts")
@EntityListeners(AuditingEntityListener.class)
public class ChartOfAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Consultant-provided or auto-generated based on number range
     *  This code can be the same for multiple company codes (legal entities) or if no company is explicitly mapped,
     *  a global config can be used for the account.
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
    private AccountSubtype subtype;


    /**
     * Indicates whether the natural balance for this account increases
     * via DEBIT or CREDIT.
     * Assets & Expenses --> DEBIT normal
     * Liabilities, Revenue, Equity --> CREDIT normal
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NormalBalance normalBalance;

    // General rule of thumb --> if Company Code is null, the configuration for this accounting entity is global -- i.e. for all company codes.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // Hierarchy (optional, supports parent-child grouping)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private ChartOfAccount parentAccount;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountCategory getCategory() {
        return category;
    }

    public void setCategory(AccountCategory category) {
        this.category = category;
    }

    public AccountSubtype getSubtype() {
        return subtype;
    }

    public void setSubtype(AccountSubtype subtype) {
        this.subtype = subtype;
    }

    public NormalBalance getNormalBalance() {
        return normalBalance;
    }

    public void setNormalBalance(NormalBalance normalBalance) {
        this.normalBalance = normalBalance;
    }

    public ChartOfAccount getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(ChartOfAccount parentAccount) {
        this.parentAccount = parentAccount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
