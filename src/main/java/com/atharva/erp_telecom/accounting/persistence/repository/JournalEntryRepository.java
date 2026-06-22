package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntryEntity,Long> {

    List<JournalEntryEntity> findBySourceTransactionIdAndEventType(String transactionId, AccountingEventType transactionType);

    // List<JournalEntryEntity> findByCompanyCodeAndPostingDateBetween(String companyId, LocalDate from, LocalDate to);
}
