package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.entity.finance.Company;
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

        // Validate Basic Null checks.
        validateNullChecks(rule);

        // Validate the effective dates.
        validateEffectiveDates(rule);

        // Validate the header conditional expression.
        validateConditionExpression(rule.getHeaderConditionExpression());

        boolean hasDebit = false;
        boolean hasCredit = false;
        Set<String> duplicates = new HashSet<>();
        Set<Integer> sortOrders = new HashSet<>();

        for (PostingRuleLine line : rule.getLines()) {
            if (line.getAccount() == null || !line.getAccount().isActive()) {
                throw new IllegalPostingRuleException("Invalid / inactive account referenced in rule.");
            }

            // Company consistency
            Company companyInPostingRule = rule.getCompany();
            Company companyInAccount = line.getAccount().getCompany();

            // Check if Company used in the CoA and Rule match. If both are null --> Global configuration for all Companies.
            if (companyInPostingRule != null && companyInAccount != null && !companyInPostingRule.getCompanyId().equals(companyInAccount.getCompanyId())) {
                throw new IllegalPostingRuleException("Line account belongs to a different company");
            }

            if (line.getEntryType() == EntryType.DEBIT) hasDebit = true;
            if (line.getEntryType() == EntryType.CREDIT) hasCredit = true;

            // Duplicate check for CREDIT/DEBIT + ACCOUNT PAIR.
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

    /**
     * Method to validate basic null checks in Posting Rule.
     * @param rule
     */
    private void validateNullChecks(PostingRule rule){
        if (rule.getPostingRuleCode() == null || rule.getPostingRuleCode().isBlank()) {
            throw new IllegalPostingRuleException("Posting rule code is required");
        }

        if (rule.getEventType() == null) {
            throw new IllegalPostingRuleException("Accounting Event Type is required");
        }

        if (rule.getLines() == null || rule.getLines().isEmpty()) {
            throw new IllegalPostingRuleException("Posting rule must contain at least one posting line");
        }

    }

    /**
     * Method to validate Conditional Expressions to be evaluated at Runtime using SPeL in Posting Rule.
     * @param expression
     */
    private void validateConditionExpression(String expression) {
        if (expression == null || expression.isBlank())
            throw new IllegalPostingRuleException("Header Condition Expression should not be null or empty.");

        if (!expression.matches(HEADER_CONDITION_ALLOWED_PATTERN)) {
            throw new IllegalPostingRuleException("Condition expression has illegal characters.");
        }
    }

    /**
     * Method to validate Amount Expressions to be evaluated at Runtime using SPeL in Posting Rule.
     * @param expr
     */
    private void validateAmountExpression(String expr) {
        if (expr == null || expr.isBlank())
            throw new IllegalPostingRuleException("Amount expression should not be null or empty.");
        if (expr.contains(";") || expr.contains("{") || expr.contains("}")) {
            throw new IllegalPostingRuleException("Amount expression contains invalid characters");
        }
    }

    /**
     * Method to validate Effective dates of a Posting Rule.
     * @param rule
     */
    private void validateEffectiveDates(PostingRule rule) {
        if (rule.getEffectiveTo() != null &&
                rule.getEffectiveTo().isBefore(rule.getEffectiveFrom())) {
            throw new IllegalPostingRuleException("Invalid effective date range");
        }
    }
}
