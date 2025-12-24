package com.atharva.erp_telecom.dto.accounting;

import com.atharva.erp_telecom.enums.AccountCategory;
import com.atharva.erp_telecom.enums.AccountSubtype;
import com.atharva.erp_telecom.enums.NormalBalance;

import java.time.LocalDate;

public class ChartOfAccountRequest {
    private String accountCode;  // Optional if number auto-generated

    private String accountName;

    private AccountCategory category;

    private AccountSubtype subtype;

    private String companyCode;

    private NormalBalance normalBalance;

    private Boolean active = true;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
