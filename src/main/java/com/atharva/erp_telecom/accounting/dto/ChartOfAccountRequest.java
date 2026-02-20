package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import com.atharva.erp_telecom.accounting.enums.AccountSubCategory;
import com.atharva.erp_telecom.accounting.enums.NormalBalance;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ChartOfAccountRequest {
    private String accountCode;  // Optional if number auto-generated

    private String accountName;

    private AccountCategory category;

    private AccountSubCategory subtype;

    private String companyCode;

    private NormalBalance normalBalance;

    private String currencyCode;

    private Boolean active = true;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

}
