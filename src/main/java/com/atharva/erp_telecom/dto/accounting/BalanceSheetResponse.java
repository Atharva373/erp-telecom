package com.atharva.erp_telecom.dto.accounting;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Data
@Getter
@Setter
public class BalanceSheetResponse {

    private List<BalanceSheetSectionRow> assets;
    private List<BalanceSheetSectionRow> liabilities;
    private List<BalanceSheetSectionRow> equity;

    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal totalEquity;
}

