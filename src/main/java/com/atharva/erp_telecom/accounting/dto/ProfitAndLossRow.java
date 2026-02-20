package com.atharva.erp_telecom.accounting.dto;


import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitAndLossRow {

    private String accountCode;
    private String accountName;
    private AccountCategory category; // REVENUE or EXPENSE
    private BigDecimal amount;

}
