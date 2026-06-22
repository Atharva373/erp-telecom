package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodPolicyEntity;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodPolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PostingPeriodPolicyService {

    private final PostingPeriodPolicyRepository repository;

    public PostingPeriodPolicyService(PostingPeriodPolicyRepository repository) {
        this.repository = repository;
    }

    public PostingPeriodPolicyEntity getEffectivePolicy(
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
