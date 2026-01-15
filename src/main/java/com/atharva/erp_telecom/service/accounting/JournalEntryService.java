package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.entity.accounting.JournalEntry;
import com.atharva.erp_telecom.entity.accounting.JournalEntryLine;
import com.atharva.erp_telecom.enums.AccountingEventType;
import com.atharva.erp_telecom.enums.EntryType;
import com.atharva.erp_telecom.repository.accounting.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    /**
     * Save a fully constructed Journal Entry.
     * The PostingEngine will create the JE object, and this method will:
     *  - validate it
     *  - ensure it is balanced
     *  - persist the JE and lines
     */
    @Transactional
    public JournalEntry save(JournalEntry je) {

        validateBalanced(je);

        // Mark JE as balanced
        je.setBalanced(true);

        return journalEntryRepository.save(je);
    }

    /**
     * Fetch one JE by ID
     */
    public JournalEntry getById(Long id) {
        return journalEntryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Journal Entry not found: " + id)
                );
    }

    /**
     * Fetch JE by source transaction
     */
    public List<JournalEntry> getBySource(Long transactionId, String transactionType) {
        return journalEntryRepository.findBySourceTransactionIdAndEventType(
                transactionId.toString(), AccountingEventType.valueOf(transactionType)
        );
    }

    /**
     * Fetch all entries for a company in a date range
     */
    public List<JournalEntry> findByCompanyAndDate(
            Long companyId, LocalDate from, LocalDate to) {

        return journalEntryRepository
                .findByCompanyIdAndPostingDateBetween(companyId, from, to);
    }

    /**
     * Reverse an existing JE by generating a new reversed document
     */
    @Transactional
    public JournalEntry reverse(Long jeId, String reversedBy) {

        JournalEntry original = getById(jeId);

        if (original.isReversed()) {
            throw new IllegalStateException("JE already reversed: " + jeId);
        }

        JournalEntry reversal = new JournalEntry();

        reversal.setCompanyId(original.getCompanyId());
        reversal.setEventType(original.getEventType());
        reversal.setPostingRuleId(original.getPostingRuleId());
        reversal.setPostingDate(LocalDateTime.now());
        reversal.setLedger(original.getLedger());

        reversal.setSourceTransactionId(original.getSourceTransactionId());
        reversal.setSourceSystem(original.getSourceSystem());

        reversal.setReversalOf(original.getId());
        reversal.setPosted(true);
        reversal.setBalanced(true);
        reversal.setCurrencyCode(original.getCurrencyCode());

        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;

        for (JournalEntryLine line : original.getLines()) {
            JournalEntryLine revLine = new JournalEntryLine();

            revLine.setJournalEntry(reversal);
            revLine.setAccountCode(line.getAccountCode());
            revLine.setAccountName(line.getAccountName());
            revLine.setSortOrder(line.getSortOrder());
            revLine.setAccountCategory(line.getAccountCategory());
            revLine.setAccountSubtype(line.getAccountSubtype());

            // Flip entry type
            if (line.getEntryType() == EntryType.DEBIT) {
                revLine.setEntryType(EntryType.CREDIT);
                revLine.setAmount(line.getAmount());
                credit = credit.add(line.getAmount());
            } else {
                revLine.setEntryType(EntryType.DEBIT);
                revLine.setAmount(line.getAmount());
                debit = debit.add(line.getAmount());
            }

            reversal.getLines().add(revLine);
        }

        reversal.setTotalDebit(debit);
        reversal.setTotalCredit(credit);

        // Mark original reversed
        original.setReversed(true);
        journalEntryRepository.save(original);

        // Save reversal
        return journalEntryRepository.save(reversal);
    }


    /**
     * Validates if JE is balanced (DR==CR)
     */
    private void validateBalanced(JournalEntry je) {
        BigDecimal dr = je.getTotalDebit();
        BigDecimal cr = je.getTotalCredit();

        if (dr == null || cr == null) {
            throw new IllegalArgumentException("Journal Entry missing totals");
        }

        if (dr.compareTo(cr) != 0) {
            throw new IllegalArgumentException(
                    "Unbalanced Journal Entry: Debit=" + dr + ", Credit=" + cr
            );
        }
    }
}
