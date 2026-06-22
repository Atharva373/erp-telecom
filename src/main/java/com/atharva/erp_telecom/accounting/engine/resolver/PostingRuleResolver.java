package com.atharva.erp_telecom.accounting.engine.resolver;

import com.atharva.erp_telecom.accounting.domain.PostingRule;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.accounting.persistence.config.PostingRuleEntity;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingRuleRepository;
import com.atharva.erp_telecom.invoicing.enums.FinanceProfileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This Service class is used to get the Posting rule corresponding to the combination of the Accounting Event + CompanyId + Posting date
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class PostingRuleResolver {

    private final PostingRuleRepository postingRuleRepository;

    public PostingRuleEntity resolveRule(PostingContext context) {

        log.info(
                "Resolving posting rule for eventType={}, companyCode={}",
                context.getEventType(),
                context.getCompanyCode()
        );

        return postingRuleRepository
                .findActiveRuleForCompany(
                        context.getEventType(),
                        context.getCompanyCode(),
                        context.getPostingDate().toLocalDate()
                )
                .or(() ->
                        postingRuleRepository
                                .findActiveGlobalRule(
                                        context.getEventType(),
                                        context.getPostingDate().toLocalDate()
                                )
                )
                .orElseThrow(() ->
                        new IllegalPostingRuleException(
                                "No posting rule found for eventType="
                                        + context.getEventType()
                                        + ", companyCode="
                                        + context.getCompanyCode()
                        )
                );
    }
}