package com.atharva.erp_telecom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ProfitAndLossResponse {

    private List<ProfitAndLossRow> revenues;
    private List<ProfitAndLossRow> expenses;

    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal netProfit; // revenue - expense
}

