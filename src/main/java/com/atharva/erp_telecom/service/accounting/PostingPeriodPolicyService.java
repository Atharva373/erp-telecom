package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriodPolicy;
import com.atharva.erp_telecom.repository.accounting.PostingPeriodPolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PostingPeriodPolicyService {

    private final PostingPeriodPolicyRepository repository;

    public PostingPeriodPolicyService(PostingPeriodPolicyRepository repository) {
        this.repository = repository;
    }

    public PostingPeriodPolicy getEffectivePolicy(
            String companyCode,
            LocalDate onDate
    ) {
        return repository.findEffectivePolicy(companyCode, onDate)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No active Posting Period Policy found for company " + companyCode
                        )
                );
    }
}
