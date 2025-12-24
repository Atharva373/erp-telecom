package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.entity.salesorder.Company;
import com.atharva.erp_telecom.entity.accounting.PostingRule;
import com.atharva.erp_telecom.entity.accounting.PostingRuleLine;
import com.atharva.erp_telecom.enums.EntryType;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PostingRuleValidator {

    private static final String HEADER_CONDITION_ALLOWED_PATTERN = "^[A-Za-z0-9_'\\s\"=<>!&|().+-]*$";

    public void validate(PostingRule rule) {
        if (rule.getPostingRuleCode() == null || rule.getPostingRuleCode().isBlank()) {
            throw new IllegalPostingRuleException("Posting rule code is required");
        }

        if (rule.getEventType() == null) {
            throw new IllegalPostingRuleException("Accounting Event Type is required");
        }

        if (rule.getItems() == null || rule.getItems().isEmpty()) {
            throw new IllegalPostingRuleException("Posting rule must contain at least one posting line");
        }

        validateEffectiveDates(rule);

        // Validate the header conditional expression.
        validateConditionExpression(rule.getHeaderConditionExpression());

        boolean hasDebit = false;
        boolean hasCredit = false;
        Set<String> duplicates = new HashSet<>();
        Set<Integer> sortOrders = new HashSet<>();

        for (PostingRuleLine line : rule.getItems()) {
            if (line.getAccount() == null || !line.getAccount().isActive()) {
                throw new IllegalPostingRuleException("Invalid / inactive account referenced in rule");
            }

            // Company consistency
            Company ruleCompany = rule.getCompany();
            Company accountCompany = line.getAccount().getCompany();

            // Check if Company used in the CoA and Rule match. If both are null --> Global configuration for all Companies.
            if (ruleCompany != null && accountCompany != null && !ruleCompany.getCompanyId().equals(accountCompany.getCompanyId())) {
                throw new IllegalPostingRuleException("Line account belongs to a different company");
            }

            if (line.getEntryType() == EntryType.DEBIT) hasDebit = true;
            if (line.getEntryType() == EntryType.CREDIT) hasCredit = true;

            String key = line.getEntryType() + line.getAccount().getAccountCode();
            if (!duplicates.add(key)) {
                throw new IllegalPostingRuleException("Duplicate account+type entry in posting rule");
            }

            // Sort order validation
            if (line.getSortOrder() != null) {
                if (line.getSortOrder() < 0) {
                    throw new IllegalPostingRuleException("Sort order must be positive");
                }
                if (!sortOrders.add(line.getSortOrder())) {
                    throw new IllegalPostingRuleException("Duplicate sort order: " + line.getSortOrder());
                }
            }

            validateAmountExpression(line.getAmountExpression());
            validateConditionExpression(line.getItemConditionExpression());
        }

        // Must have both debit and credit
        if (!hasDebit || !hasCredit) {
            throw new IllegalPostingRuleException("Posting rule must include at least one DEBIT and one CREDIT line");
        }


    }

    private void validateConditionExpression(String headerConditionExpression) {
        if (headerConditionExpression == null || headerConditionExpression.isBlank()) return;

        if (!headerConditionExpression.matches(HEADER_CONDITION_ALLOWED_PATTERN)) {
            throw new IllegalPostingRuleException("Condition expression has illegal characters");
        }
    }

    private void validateAmountExpression(String expr) {
        if (expr == null || expr.isBlank()) return;
        if (expr.contains(";") || expr.contains("{") || expr.contains("}")) {
            throw new IllegalPostingRuleException("Amount expression contains invalid characters");
        }
    }

    private void validateEffectiveDates(PostingRule rule) {
        if (rule.getEffectiveTo() != null &&
                rule.getEffectiveTo().isBefore(rule.getEffectiveFrom())) {
            throw new IllegalPostingRuleException("Invalid effective date range");
        }
    }
}
