package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.entity.accounting.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Long> {

    List<JournalEntry> findBySourceTransactionIdAndSourceTransactionType(Long transactionId, String transactionType);

    List<JournalEntry> findByCompanyIdAndPostingDateBetween(Long companyId, LocalDate from, LocalDate to);
}
