package com.atharva.erp_telecom.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class BalanceSheetSectionRow {

    private String accountCode;
    private String accountName;
    private String category; // ASSET, LIABILITY, EQUITY
    private BigDecimal balance;

}

