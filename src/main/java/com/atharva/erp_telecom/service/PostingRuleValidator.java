package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.entity.PostingRule;
import com.atharva.erp_telecom.entity.PostingRuleLine;
import com.atharva.erp_telecom.enums.EntryType;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PostingRuleValidator {

    public void validate(PostingRule rule) {
        if (rule.getItems() == null || rule.getItems().isEmpty()) {
            throw new IllegalPostingRuleException("Posting rule must contain at least one line");
        }
    
        boolean hasDebit = false;
        boolean hasCredit = false;
        Set<String> duplicates = new HashSet<>();
    
        for (PostingRuleLine line : rule.getItems()) {
            if (line.getAccount() == null || !line.getAccount().isActive()) {
                throw new IllegalPostingRuleException("Invalid account referenced in rule");
            }

            if (line.getEntryType() == EntryType.DEBIT) hasDebit = true;
            if (line.getEntryType() == EntryType.CREDIT) hasCredit = true;

            String key = line.getEntryType() + line.getAccount().getAccountCode();
            if (!duplicates.add(key)) {
                throw new IllegalPostingRuleException("Duplicate account+type entry in posting rule");
            }

            validateExpression(line.getAmountExpression());
        }

        if (!hasDebit && !hasCredit) {
        throw new IllegalPostingRuleException("Posting rule must have at least one debit and one credit");
        }
    
        validateEffectiveDates(rule);
    }

private void validateExpression(String expr) {
    // Placeholder: compile or evaluate with sandboxed evaluator
}

private void validateEffectiveDates(PostingRule rule) {
    if (rule.getEffectiveTo() != null &&
            rule.getEffectiveTo().isBefore(rule.getEffectiveFrom())) {
        throw new IllegalPostingRuleException("Invalid effective date range");
    }
}
}
