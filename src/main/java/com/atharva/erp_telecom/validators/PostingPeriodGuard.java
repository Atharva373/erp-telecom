package com.atharva.erp_telecom.validators;

import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.entity.accounting.PostingPeriodPolicy;
import com.atharva.erp_telecom.enums.PeriodStatus;
import com.atharva.erp_telecom.exception.custom_exceptions.PostingPeriodException;
import com.atharva.erp_telecom.service.accounting.PeriodStateResolver;
import com.atharva.erp_telecom.service.accounting.PostingPeriodPolicyService;
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
            PostingPeriod period,
            String companyCode,
            LocalDate eventDate,
            boolean isReversal
    ) {
        PostingPeriodPolicy policy =
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

