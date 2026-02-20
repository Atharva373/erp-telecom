package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.dto.TrialBalanceRow;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialBalanceRepository extends JpaRepository<JournalEntryLineEntity, Long> {

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
            AND je.currencyCode = :currencyEntity
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
            @Param("currencyEntity") String currencyEntity
    );
}
