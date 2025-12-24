package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.dto.accounting.TrialBalanceRow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialBalanceRepository {

    @Query("""
        SELECT new com.atharva.erp_telecom.dto.TrialBalanceRow(
            jel.accountCode,
            jel.accountName,
            jel.accountCategory,
            SUM(CASE WHEN jel.entryType = 'DEBIT' THEN jel.amount ELSE 0 END),
            SUM(CASE WHEN jel.entryType = 'CREDIT' THEN jel.amount ELSE 0 END)
        )
        FROM JournalEntryLine jel
        JOIN jel.journalEntry je
        WHERE
            je.companyId = :companyId
            AND je.fiscalYear = :fiscalYear
            AND je.postingPeriod <= :postingPeriod
            AND je.currencyCode = :currency
            AND je.posted = true
        GROUP BY
            jel.accountCode,
            jel.accountName,
            jel.accountCategory
        ORDER BY jel.accountCode
    """)
    List<TrialBalanceRow> fetchTrialBalance(
            Long companyId,
            Integer fiscalYear,
            Integer postingPeriod,
            String currency
    );
}
