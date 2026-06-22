package com.atharva.erp_telecom.accounting.validators;

import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import org.springframework.stereotype.Component;

@Component
public class BalancedJournalValidator {

    public void validate(JournalEntryEntity journal) {
        if (journal.getTotalDebit().compareTo(journal.getTotalCredit()) != 0){
            throw new IllegalPostingRuleException(
                    "Unbalanced Journal Entry. Debit=" + journal.getTotalDebit() + ", Credit="+ journal.getTotalCredit() + " | for source transaction ID:" + journal.getSourceTransactionId()
            );
        }
    }
}