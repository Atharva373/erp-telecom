package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.dto.accounting.TrialBalanceRow;
import com.atharva.erp_telecom.entity.accounting.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialBalanceRepository extends JpaRepository<JournalEntryLine, Long> {

    @Query("""
        SELECT new com.atharva.erp_telecom.dto.accounting.TrialBalanceRow(
            jel.accountCode,
            jel.accountName,
            jel.accountCategory,
            SUM(CASE WHEN jel.entryType = com.atharva.erp_telecom.enums.EntryType.DEBIT
                     THEN jel.amount ELSE 0 END),
            SUM(CASE WHEN jel.entryType = com.atharva.erp_telecom.enums.EntryType.CREDIT
                     THEN jel.amount ELSE 0 END)
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
            @Param("companyId") Long companyId,
            @Param("fiscalYear") Integer fiscalYear,
            @Param("postingPeriod") Integer postingPeriod,
            @Param("currency") String currency
    );
}
