package com.atharva.erp_telecom.accounting.engine;

import com.atharva.erp_telecom.accounting.service.*;
import com.atharva.erp_telecom.accounting.dto.PostingContext;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryLineEntity;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingRuleEntity;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingRuleLineEntity;
import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import com.atharva.erp_telecom.accounting.enums.AccountSubCategory;
import com.atharva.erp_telecom.accounting.enums.EntryType;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import com.atharva.erp_telecom.finance.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class PostingEngine {

    private final PostingRuleResolver ruleResolver;
    private final PostingExpressionEvaluator evaluator;
    private final JournalEntryService journalEntryService;
    private final CurrencyService currencyService;
    private final PostingPeriodService postingPeriodService;

    @Autowired
    public PostingEngine(
            PostingRuleResolver ruleResolver,
            PostingExpressionEvaluator evaluator,
            JournalEntryService journalEntryService, CurrencyService currencyService, PostingPeriodService postingPeriodService
    ) {
        this.ruleResolver = ruleResolver;
        this.evaluator = evaluator;
        this.journalEntryService = journalEntryService;
        this.currencyService = currencyService;
        this.postingPeriodService = postingPeriodService;
    }


    /**
     * Main posting function:
     * - Resolve applicable posting rule
     * - Evaluate header + item conditions
     * - Evaluate line expressions
     * - Build JournalEntryEntity + Lines
     * - Validate DR = CR
     * - Persist using JournalEntryService
     */
    @Transactional
    public JournalEntryEntity post(PostingContext ctx) {
        JournalEntryEntity jeToPersist = buildJournalEntry(ctx);
        return journalEntryService.save(jeToPersist);
    }

    // Method to just simulate (but not persist) Journal Entries. Does same thing as post() except persisting to DB.
    public JournalEntryEntity simulate(PostingContext ctx) {
        return buildJournalEntry(ctx);
    }


    public JournalEntryEntity buildJournalEntry(PostingContext ctx){
        // 1️⃣ Resolve posting rule (company-specific or global)
        PostingRuleEntity rule = ruleResolver.resolveRule(ctx);

        // 2️⃣ Build empty JE header
        JournalEntryEntity je = new JournalEntryEntity();
        je.setCompanyCode(ctx.getCompanyCode());
        je.setEventType(ctx.getEventType());
        je.setPostingRuleId(rule.getId());
        je.setLedger("PRIMARY");
        je.setPostingDate(ctx.getPostingDate());

        // 3️⃣ Optional: source transaction mappings
        // je.setSourceTransactionId(ctx.get());
        // je.setSourceTransactionType(ctx.getSourceTransactionType());

        postingPeriodService.assertOpen(
                ctx.getCompanyCode(),
                ctx.getPostingDate().getYear(),
                ctx.getPostingDate().getMonthValue()
        );

        // Running totals
        BigDecimal totalDr = BigDecimal.ZERO;
        BigDecimal totalCr = BigDecimal.ZERO;

        // 4️⃣ Sort lines by sortOrder (nulls last)
        List<PostingRuleLineEntity> linesToApply =
                rule.getLines().stream()
                        .sorted(Comparator
                                .comparing(
                                        PostingRuleLineEntity::getSortOrder,
                                        Comparator.nullsLast(Integer::compareTo)
                                )
                        )
                        .toList();

        // 5️⃣ Apply rule-level condition (if any)
        boolean headerConditionPass =
                evaluator.evaluateCondition(rule.getHeaderConditionExpression(), ctx);

        if (!headerConditionPass) {
            throw new IllegalPostingRuleException(
                    "PostingRuleEntity header condition evaluated to FALSE for rule: " + rule.getPostingRuleCode()
            );
        }

        // 6️⃣ Process each posting line
        for (PostingRuleLineEntity line : linesToApply) {

            // Skip line if its condition fails
            if (!evaluator.evaluateCondition(line.getItemConditionExpression(), ctx)) {
                continue;
            }

            // Amount evaluation
            BigDecimal amount = evaluator.evaluateAmount(line.getAmountExpression(), ctx);
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                continue; // No impact
            }

            // 7️⃣ Build JournalEntryLineEntity
            JournalEntryLineEntity jeLine = new JournalEntryLineEntity();
            jeLine.setJournalEntryEntity(je);
            jeLine.setSortOrder(line.getSortOrder());
            jeLine.setAccountCode(line.getAccount().getAccountCode());
            jeLine.setAccountName(line.getAccount().getAccountName());
            jeLine.setAmount(amount);
            jeLine.setEntryType(line.getEntryType());

            // Denormalized metadata
            jeLine.setAccountCategory(AccountCategory.valueOf(line.getAccount().getCategory().name()));
            if (line.getAccount().getSubtype() != null)
                jeLine.setAccountSubCategory(AccountSubCategory.valueOf(line.getAccount().getSubtype().name()));

            je.getLines().add(jeLine);

            // Track totals
            if (line.getEntryType() == EntryType.DEBIT) {
                totalDr = totalDr.add(amount);
            } else {
                totalCr = totalCr.add(amount);
            }
        }

        // 8️⃣ Set totals
        je.setTotalDebit(totalDr);
        je.setTotalCredit(totalCr);

        // 9️⃣ Balanced check
        if (totalDr.compareTo(totalCr) != 0) {
            throw new IllegalPostingRuleException(
                    "Unbalanced JE. Debit=" + totalDr + ", Credit=" + totalCr
            );
        }

        je.setBalanced(true);
        return je;
    }
}
