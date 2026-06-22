package com.atharva.erp_telecom.accounting.validators;

import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodPolicyEntity;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.exception.custom_exceptions.PostingPeriodException;
import com.atharva.erp_telecom.accounting.service.PeriodStateResolver;
import com.atharva.erp_telecom.accounting.service.PostingPeriodPolicyService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PostingPeriodGuard {

    private final PostingPeriodPolicyService policyService;
    private final PeriodStateResolver stateResolver;

    public PostingPeriodGuard(
            PostingPeriodPolicyService policyService,
            PeriodStateResolver stateResolver
    ) {
        this.policyService = policyService;
        this.stateResolver = stateResolver;
    }

    public void assertPostingAllowed(
            PostingPeriodEntity period,
            String companyCode,
            LocalDate eventDate,
            boolean isReversal
    ) {
        PostingPeriodPolicyEntity policy =
                policyService.getEffectivePolicy(companyCode, eventDate);

        PeriodStatus state =
                stateResolver.resolve(period, policy, LocalDateTime.now());

        switch (state) {
            case OPEN -> {
                return;
            }
            case CLOSED -> {
                if (!isReversal) {
                    throw new PostingPeriodException("Cannot post new transactions. Period already in CLOSED state. Only reversals allowed.");
                }
            }
            case BUFFER_LOCKED -> {
                throw new PostingPeriodException("Cannot post new transactions in. Period already in BUFFER CLOSED state. View only operations allowed.");
            }
            case LOCKED -> {
                throw new PostingPeriodException("Cannot post new transactions.Period already in LOCKED status.");
            }
        }
    }
}

