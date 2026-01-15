package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.dto.accounting.BalanceSheetSectionRow;
import com.atharva.erp_telecom.dto.accounting.ProfitAndLossRow;
import com.atharva.erp_telecom.entity.accounting.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialStatementsRepository extends JpaRepository<JournalEntryLine,Long> {

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
        SELECT new com.atharva.erp_telecom.dto.accounting.BalanceSheetSectionRow(
            jel.accountCode,
            jel.accountName,
            jel.accountCategory,
            SUM(
                CASE
                    WHEN jel.entryType = com.atharva.erp_telecom.enums.EntryType.DEBIT
                    THEN jel.amount
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
            AND jel.accountCategory IN (
                com.atharva.erp_telecom.enums.AccountCategory.ASSET,
                com.atharva.erp_telecom.enums.AccountCategory.LIABILITY,
                com.atharva.erp_telecom.enums.AccountCategory.EQUITY
            )
            AND je.posted = true
        GROUP BY
            jel.accountCode,
            jel.accountName,
            jel.accountCategory
        ORDER BY jel.accountCode
    """)
    List<BalanceSheetSectionRow> fetchBalanceSheet(
            @Param("companyId") Long companyId,
            @Param("fiscalYear") Integer fiscalYear,
            @Param("postingPeriod") Integer postingPeriod,
            @Param("currency") String currency
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
        SELECT new com.atharva.erp_telecom.dto.accounting.ProfitAndLossRow(
            jel.accountCode,
            jel.accountName,
            jel.accountCategory,
            SUM(
                CASE
                    WHEN jel.entryType = com.atharva.erp_telecom.enums.EntryType.CREDIT
                    THEN jel.amount
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
            AND jel.accountCategory IN (
                com.atharva.erp_telecom.enums.AccountCategory.REVENUE,
                com.atharva.erp_telecom.enums.AccountCategory.EXPENSE
            )
            AND je.posted = true
        GROUP BY
            jel.accountCode,
            jel.accountName,
            jel.accountCategory
        ORDER BY jel.accountCode
    """)
    List<ProfitAndLossRow> fetchProfitAndLoss(
            @Param("companyId") Long companyId,
            @Param("fiscalYear") Integer fiscalYear,
            @Param("fromPeriod") Integer fromPeriod,
            @Param("toPeriod") Integer toPeriod,
            @Param("currency") String currency
    );


}
