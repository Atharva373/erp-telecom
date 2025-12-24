package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.BalanceSheetResponse;
import com.atharva.erp_telecom.dto.accounting.BalanceSheetSectionRow;
import com.atharva.erp_telecom.enums.AccountCategory;
import com.atharva.erp_telecom.repository.accounting.FinancialStatementsRepository;
import com.atharva.erp_telecom.utils.GenericUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BalanceSheetService {

    private final FinancialStatementsRepository repo;

    public BalanceSheetService(FinancialStatementsRepository repo) {
        this.repo = repo;
    }

    public BalanceSheetResponse generate(
            Long companyId,
            Integer fiscalYear,
            Integer postingPeriod,
            String currency
    ) {
        List<BalanceSheetSectionRow> rows =
                repo.fetchBalanceSheet(companyId, fiscalYear, postingPeriod, currency);

        BalanceSheetResponse resp = new BalanceSheetResponse();

        resp.setAssets(GenericUtils.filterRows(rows,row -> Objects.equals(row.getCategory(), AccountCategory.ASSET.toString())));
        resp.setLiabilities(GenericUtils.filterRows(rows,row -> Objects.equals(row.getCategory(), AccountCategory.LIABILITY.toString())));
        resp.setEquity(GenericUtils.filterRows(rows,row -> Objects.equals(row.getCategory(), AccountCategory.EQUITY.toString())));

        resp.setTotalAssets(
                GenericUtils.sum(resp.getAssets(), BalanceSheetSectionRow::getBalance)
        );

        resp.setTotalLiabilities(
                GenericUtils.sum(resp.getLiabilities(), BalanceSheetSectionRow::getBalance)
        );

        resp.setTotalEquity(
                GenericUtils.sum(resp.getEquity(), BalanceSheetSectionRow::getBalance)
        );


        return resp;
    }
}

