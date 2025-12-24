package com.atharva.erp_telecom.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitAndLossRow {

    private String accountCode;
    private String accountName;
    private String category; // REVENUE or EXPENSE
    private BigDecimal amount;

}
