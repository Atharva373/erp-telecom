package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.dto.PostingContext;
import com.atharva.erp_telecom.entity.JournalEntry;
import com.atharva.erp_telecom.entity.JournalEntryLine;
import com.atharva.erp_telecom.entity.PostingRule;
import com.atharva.erp_telecom.entity.PostingRuleLine;
import com.atharva.erp_telecom.enums.AccountCategory;
import com.atharva.erp_telecom.enums.AccountSubtype;
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
    private final ExpressionEvaluator evaluator;
    private final JournalEntryService journalEntryService;

    @Autowired
    public PostingEngine(
            PostingRuleResolver ruleResolver,
            ExpressionEvaluator evaluator,
            JournalEntryService journalEntryService
    ) {
        this.ruleResolver = ruleResolver;
        this.evaluator = evaluator;
        this.journalEntryService = journalEntryService;
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

        // 1️⃣ Resolve posting rule (company-specific or global)
        PostingRule rule = ruleResolver.resolveRule(ctx);

        // 2️⃣ Build empty JE header
        JournalEntry je = new JournalEntry();
        je.setCompanyId(ctx.getCompanyId());
        je.setEventType(ctx.getEventType());
        je.setPostingRuleId(rule.getId());
        je.setLedger("PRIMARY");
        je.setPostingDate(ctx.getPostingDate());

        // 3️⃣ Optional: source transaction mappings
        // je.setSourceTransactionId(ctx.get());
        // je.setSourceTransactionType(ctx.getSourceTransactionType());

        // Running totals
        BigDecimal totalDr = BigDecimal.ZERO;
        BigDecimal totalCr = BigDecimal.ZERO;

        // 4️⃣ Sort lines by sortOrder (nulls last)
        List<PostingRuleLine> linesToApply =
                rule.getItems().stream()
                        .sorted(Comparator.comparing(
                                PostingRuleLine::getSortOrder,
                                Comparator.nullsLast(Integer::compareTo)
                        ))
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
                jeLine.setAccountSubtype(AccountSubtype.valueOf(line.getAccount().getSubtype().name()));

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

        // 🔟 Persist
        return journalEntryService.save(je);
    }
}
