package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.dto.accounting.BalanceSheetSectionRow;
import com.atharva.erp_telecom.dto.accounting.ProfitAndLossRow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialStatementsRepository {

    /**
     * Balance Sheet answers:
     * “What do we own and owe at a point in time?”
     * @param companyId
     * @param fiscalYear
     * @param postingPeriod
     * @param currency
     * @return
     */
    @Query("""
    SELECT new com.atharva.erp_telecom.dto.BalanceSheetSectionRow(
        jel.accountCode,
        jel.accountName,
        jel.accountCategory,
        SUM(
            CASE
                WHEN jel.entryType = 'DEBIT' THEN jel.amount
                ELSE -jel.amount
            END
        )
    )
    FROM JournalEntryLine jel
    JOIN jel.journalEntry je
    WHERE
        je.companyId = :companyId
        AND je.fiscalYear = :fiscalYear
        AND je.postingPeriod <= :postingPeriod
        AND je.currencyCode = :currency
        AND jel.accountCategory IN ('ASSET','LIABILITY','EQUITY')
    GROUP BY
        jel.accountCode, jel.accountName, jel.accountCategory
""")
    List<BalanceSheetSectionRow> fetchBalanceSheet(
            Long companyId,
            Integer fiscalYear,
            Integer postingPeriod,
            String currency
    );

    /**
     * Profit & Loss answers:
     * “How did we perform over a period of time?”
     * @param companyId
     * @param fiscalYear
     * @param fromPeriod
     * @param toPeriod
     * @param currency
     * @return
     */
    @Query("""
    SELECT new com.atharva.erp_telecom.dto.ProfitAndLossRow(
        jel.accountCode,
        jel.accountName,
        jel.accountCategory,
        SUM(
            CASE
                WHEN jel.entryType = 'CREDIT' THEN jel.amount
                ELSE -jel.amount
            END
        )
    )
    FROM JournalEntryLine jel
    JOIN jel.journalEntry je
    WHERE
        je.companyId = :companyId
        AND je.fiscalYear = :fiscalYear
        AND je.postingPeriod BETWEEN :fromPeriod AND :toPeriod
        AND je.currencyCode = :currency
        AND jel.accountCategory IN ('REVENUE','EXPENSE')
    GROUP BY
        jel.accountCode, jel.accountName, jel.accountCategory
""")
    List<ProfitAndLossRow> fetchProfitAndLoss(
            Long companyId,
            Integer fiscalYear,
            Integer fromPeriod,
            Integer toPeriod,
            String currency
    );


}
