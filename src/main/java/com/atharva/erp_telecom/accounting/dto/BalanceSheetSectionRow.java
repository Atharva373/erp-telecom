package com.atharva.erp_telecom.accounting.dto;

import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceSheetSectionRow {

    private String accountCode;
    private String accountName;
    private AccountCategory category; // ASSET, LIABILITY, EQUITY
    private BigDecimal balance;

}

