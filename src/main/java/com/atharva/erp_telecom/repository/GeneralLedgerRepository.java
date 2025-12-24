package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.dto.GLLineRow;
import com.atharva.erp_telecom.entity.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralLedgerRepository
        extends JpaRepository<JournalEntryLine, Long> {

    // JPQL Query binding A DTO to its parent Entity using a Database query.
    // Generic format: SELECT new <fully-qualified-class-name>(<field1>, <field2>, ...) FROM <Entity> e WHERE ...
    @Query("""
        SELECT new com.atharva.erp_telecom.dto.GLLineRow(
            je.postingDate,
            je.id,
            jel.entryType,
            jel.amount,
            je.sourceTransactionType,
            je.sourceTransactionId
        )
        FROM JournalEntryLine jel
        JOIN jel.journalEntry je
        WHERE
            je.companyId = :companyId
            AND jel.accountCode = :accountCode
            AND je.fiscalYear = :fiscalYear
            AND je.postingPeriod <= :postingPeriod
            AND je.currencyCode = :currency
            AND je.posted = true
        ORDER BY je.postingDate, je.id, jel.sortOrder
    """)
    List<GLLineRow> fetchGL(
            Long companyId,
            String accountCode,
            Integer fiscalYear,
            Integer postingPeriod,
            String currency
    );
}
