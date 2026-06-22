package com.atharva.erp_telecom.accounting.engine;

import com.atharva.erp_telecom.accounting.engine.builder.JournalBuilder;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.persistence.config.PostingRuleEntity;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.accounting.service.JournalEntryService;
import com.atharva.erp_telecom.accounting.service.PostingPeriodService;
import com.atharva.erp_telecom.accounting.engine.resolver.PostingRuleResolver;
import com.atharva.erp_telecom.accounting.validators.BalancedJournalValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostingEngine {

    private final PostingRuleResolver ruleResolver;
    private final JournalBuilder journalBuilder;
    private final PostingPeriodService postingPeriodService;
    private final BalancedJournalValidator balancedJournalValidator;
    private final JournalEntryService journalEntryService;


    @Transactional
    public JournalEntryEntity post(PostingContext context) {
        JournalEntryEntity journal = buildValidatedJournal(context);
        return journalEntryService.save(journal);
    }


    public JournalEntryEntity simulate(PostingContext context){
        return buildValidatedJournal(context);
    }

    // Common part between simulate and actual commit
    private JournalEntryEntity buildValidatedJournal(PostingContext context) {
        log.info(
                "Starting accounting posting for txId={}, eventType={}",
                context.getSourceTransactionId(),
                context.getEventType()
        );

        PostingRuleEntity rule = ruleResolver.resolveRule(context);
        PostingPeriodEntity requiredPeriod = postingPeriodService.getRequiredOpenPeriod(context.getCompanyCode(), context.getPostingDate().toLocalDate());
        JournalEntryEntity journal = journalBuilder.build(context,rule,requiredPeriod);

        balancedJournalValidator.validate(journal);
        journal.setBalanced(true);
        return journal;
    }
}