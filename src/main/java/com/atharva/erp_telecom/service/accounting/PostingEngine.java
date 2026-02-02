package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.PostingContext;
import com.atharva.erp_telecom.entity.accounting.JournalEntry;
import com.atharva.erp_telecom.entity.accounting.JournalEntryLine;
import com.atharva.erp_telecom.entity.accounting.PostingRule;
import com.atharva.erp_telecom.entity.accounting.PostingRuleLine;
import com.atharva.erp_telecom.enums.AccountCategory;
import com.atharva.erp_telecom.enums.AccountSubCategory;
import com.atharva.erp_telecom.enums.EntryType;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
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


    // Config beans
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
     * - Build JournalEntry + Lines
     * - Validate DR = CR
     * - Persist using JournalEntryService
     */
    @Transactional
    public JournalEntry post(PostingContext ctx) {
        JournalEntry jeToPersist = buildJournalEntry(ctx);
        return journalEntryService.save(jeToPersist);
    }

    // Method to just simulate (but not persist) Journal Entries. Does same thing as post() except persisting to DB.
    public JournalEntry simulate(PostingContext ctx) {
        return buildJournalEntry(ctx);
    }


    public JournalEntry buildJournalEntry(PostingContext ctx){
        // 1️⃣ Resolve posting rule (company-specific or global)
        PostingRule rule = ruleResolver.resolveRule(ctx);

        // 2️⃣ Build empty JE header
        JournalEntry je = new JournalEntry();
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
        List<PostingRuleLine> linesToApply =
                rule.getLines().stream()
                        .sorted(Comparator
                                .comparing(
                                        PostingRuleLine::getSortOrder,
                                        Comparator.nullsLast(Integer::compareTo)
                                )
                        )
                        .toList();

        // 5️⃣ Apply rule-level condition (if any)
        boolean headerConditionPass =
                evaluator.evaluateCondition(rule.getHeaderConditionExpression(), ctx);

        if (!headerConditionPass) {
            throw new IllegalPostingRuleException(
                    "PostingRule header condition evaluated to FALSE for rule: " + rule.getPostingRuleCode()
            );
        }

        // 6️⃣ Process each posting line
        for (PostingRuleLine line : linesToApply) {

            // Skip line if its condition fails
            if (!evaluator.evaluateCondition(line.getItemConditionExpression(), ctx)) {
                continue;
            }

            // Amount evaluation
            BigDecimal amount = evaluator.evaluateAmount(line.getAmountExpression(), ctx);
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                continue; // No impact
            }

            // 7️⃣ Build JournalEntryLine
            JournalEntryLine jeLine = new JournalEntryLine();
            jeLine.setJournalEntry(je);
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
