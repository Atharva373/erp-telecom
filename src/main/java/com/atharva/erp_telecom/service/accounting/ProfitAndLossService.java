package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.ProfitAndLossResponse;
import com.atharva.erp_telecom.dto.accounting.ProfitAndLossRow;
import com.atharva.erp_telecom.enums.AccountCategory;
import com.atharva.erp_telecom.repository.accounting.FinancialStatementsRepository;
import com.atharva.erp_telecom.utils.GenericUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProfitAndLossService {

    private final FinancialStatementsRepository repo;

    public ProfitAndLossService(FinancialStatementsRepository repo) {
        this.repo = repo;
    }

    public ProfitAndLossResponse generate(
            Long companyId,
            Integer fiscalYear,
            Integer fromPeriod,
            Integer toPeriod,
            String currency
    ) {
        List<ProfitAndLossRow> rows =
                repo.fetchProfitAndLoss(companyId, fiscalYear, fromPeriod, toPeriod, currency);

        ProfitAndLossResponse resp = new ProfitAndLossResponse();

        resp.setRevenues(GenericUtils.filterRows(rows, row -> Objects.equals(row.getCategory(), AccountCategory.REVENUE.toString())));
        resp.setExpenses(GenericUtils.filterRows(rows, row -> Objects.equals(row.getCategory(), AccountCategory.EXPENSE.toString())));

        resp.setTotalRevenue(
                GenericUtils.sum(resp.getRevenues(), ProfitAndLossRow::getAmount)
        );

        resp.setTotalExpense(
                GenericUtils.sum(resp.getExpenses(), ProfitAndLossRow::getAmount)
        );
        resp.setNetProfit(
                resp.getTotalRevenue().subtract(resp.getTotalExpense())
        );

        return resp;
    }
}

