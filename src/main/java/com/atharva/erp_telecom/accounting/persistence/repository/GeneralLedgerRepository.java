//package com.atharva.erp_telecom.repository.accounting;
//
//import com.atharva.erp_telecom.accounting.dto.GLLineRow;
//import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryLineEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface GeneralLedgerRepository
//        extends JpaRepository<JournalEntryLineEntity, Long> {
//    // JPQL Query binding A DTO to its parent Entity using a Database query.
//    // Generic format: SELECT new <fully-qualified-class-name>(<field1>, <field2>, ...) FROM <Entity> e WHERE ...
//    /**
//     * General Ledger:
//     * Transaction-level detail for a single account
//     */
//    @Query("""
//        SELECT new com.atharva.erp_telecom.accounting.dto.GLLineRow(
//            je.postingDate,
//            je.id,
//            jel.entryType,
//            jel.amount,
//            je.eventType,
//            je.sourceTransactionId
//        )
//        FROM JournalEntryLineEntity jel
//        JOIN jel.journalEntry je
//        WHERE
//            je.companyId = :companyId
//            AND jel.accountCode = :accountCode
//            AND je.fiscalYear = :fiscalYear
//            AND je.postingPeriod <= :postingPeriod
//            AND je.currencyCode = :currency
//            AND je.posted = true
//        ORDER BY
//            je.postingDate,
//            je.id,
//            jel.sortOrder
//    """)
//    List<GLLineRow> fetchGL(
//            @Param("companyId") Long companyId,
//            @Param("accountCode") String accountCode,
//            @Param("fiscalYear") Integer fiscalYear,
//            @Param("postingPeriod") Integer postingPeriod,
//            @Param("currency") String currency
//    );
//}
