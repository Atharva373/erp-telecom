package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.dto.PostingContext;
import com.atharva.erp_telecom.entity.PostingRule;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import com.atharva.erp_telecom.repository.PostingRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This Service class is used to get the Posting rule corresponding to the combination of the Accounting Event + CompanyId + Posting date
 */
@Service
public class PostingRuleResolver {

    private final PostingRuleRepository repository;

    public PostingRuleResolver(PostingRuleRepository repository) {
        this.repository = repository;
    }


    public PostingRule resolveRule(PostingContext ctx) {

        // To-do: Later add - Currency Code in the search params (WHERE clause of the query).

        // 1. Try to find company-specific rule
        Optional<PostingRule> companyRule =
                repository.findActiveRuleForCompany(
                        ctx.getEventType(),
                        ctx.getCompanyId(),
                        ctx.getPostingDate()
                );

        // If not null return the value OR Fallback to global rule (company_id null)
        return companyRule.orElseGet(() -> repository.findActiveGlobalRule(
                        ctx.getEventType(),
                        ctx.getPostingDate()
                )
                .orElseThrow(() -> new IllegalPostingRuleException(
                        "No posting rule found for event " + ctx.getEventType()
                )));


    }
}
