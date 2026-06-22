package com.atharva.erp_telecom.accounting.engine.builder;

import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import com.atharva.erp_telecom.accounting.enums.AccountSubCategory;
import com.atharva.erp_telecom.accounting.enums.EntryType;
import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.persistence.config.PostingRuleEntity;
import com.atharva.erp_telecom.accounting.persistence.config.PostingRuleLineEntity;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryEntity;
import com.atharva.erp_telecom.accounting.persistence.transactional.JournalEntryLineEntity;
import com.atharva.erp_telecom.accounting.engine.evaluator.PostingExpressionEvaluator;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

// Validation and Journal/Journal item builder class.
@Component
@RequiredArgsConstructor
public class JournalBuilder {

    private final PostingExpressionEvaluator evaluator;

    public JournalEntryEntity build(PostingContext ctx,PostingRuleEntity rule,PostingPeriodEntity period){

        JournalEntryEntity je = buildHeader(ctx, rule, period);
        BigDecimal totalDr = BigDecimal.ZERO;
        BigDecimal totalCr =BigDecimal.ZERO;

        // Sort the PostingRule Lines based on the Sort Order
        List<PostingRuleLineEntity> linesToApply = rule.getLines().stream()
                        .sorted(Comparator.comparing(
                                        PostingRuleLineEntity::getSortOrder,
                                        Comparator.nullsLast(Integer::compareTo)
                                )
                        ).toList();

        // Validate the header-level/primary posting condition.
        boolean headerConditionPass = evaluator.evaluateCondition(rule.getHeaderConditionExpression(),ctx);
        if (!headerConditionPass) {
            throw new IllegalPostingRuleException("PostingRule header condition evaluated to FALSE for rule: "+ rule.getPostingRuleCode());
        }

        // Evaluate the Line item level posting condition.
        for (PostingRuleLineEntity line : linesToApply){
            if (!evaluator.evaluateCondition(line.getItemConditionExpression(),ctx)){
                continue;
            }
            BigDecimal amount = evaluator.evaluateAmount(line.getAmountExpression(),ctx);
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) continue;

            JournalEntryLineEntity jeLine = buildLine(je, line, amount);
            je.getLines().add(jeLine);

            if (line.getEntryType() == EntryType.DEBIT) totalDr = totalDr.add(amount);
            else totalCr = totalCr.add(amount);
        }

        // After Validation is successful, set the amounts.
        je.setTotalDebit(totalDr);
        je.setTotalCredit(totalCr);

        return je;
    }

    // Set Journal Header non-amount related data
    private JournalEntryEntity buildHeader(PostingContext ctx,PostingRuleEntity rule,PostingPeriodEntity period){
        JournalEntryEntity je = new JournalEntryEntity();
        je.setCompanyCode(ctx.getCompanyCode());
        je.setEventType(ctx.getEventType());
        je.setPostingRuleId(rule.getId());
        je.setLedger("PRIMARY");
        je.setPostingDate(ctx.getPostingDate());
        je.setPostingPeriod(period.getPostingPeriod());
        je.setFiscalYear(period.getFiscalYear());
        je.setCurrencyCode(ctx.getCurrencyCode());
        je.setSourceTransactionId(ctx.getSourceTransactionId());
        return je;
    }

    // Set item-level data
    private JournalEntryLineEntity buildLine(JournalEntryEntity je, PostingRuleLineEntity line, BigDecimal amount) {

        JournalEntryLineEntity jeLine = new JournalEntryLineEntity();

        jeLine.setJournalEntryEntity(je);
        jeLine.setSortOrder(line.getSortOrder());
        jeLine.setAccountCode(line.getAccount().getAccountCode());
        jeLine.setAccountName(line.getAccount().getAccountName());
        jeLine.setAmount(amount);
        jeLine.setEntryType(line.getEntryType());
        jeLine.setAccountCategory(AccountCategory.valueOf(line.getAccount().getCategory().name()));
        if (line.getAccount().getSubtype() != null) {
            jeLine.setAccountSubCategory(AccountSubCategory.valueOf(line.getAccount().getSubtype().name()));
        }
        return jeLine;
    }
}