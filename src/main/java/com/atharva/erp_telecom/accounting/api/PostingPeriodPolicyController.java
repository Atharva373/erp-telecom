package com.atharva.erp_telecom.accounting.api;

import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingPeriodPolicyEntity;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodPolicyRepository;
import com.atharva.erp_telecom.accounting.service.PostingPeriodPolicyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/masterdata/finance/accounting/posting-period-policy")
@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
public class PostingPeriodPolicyController {

    private final PostingPeriodPolicyService service;
    private final PostingPeriodPolicyRepository repository;

    public PostingPeriodPolicyController(
            PostingPeriodPolicyService service,
            PostingPeriodPolicyRepository repository
    ) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public PostingPeriodPolicyEntity create(@RequestBody PostingPeriodPolicyEntity policy) {
        // validate policy ranges if needed
        return repository.save(policy);
    }

    @GetMapping("/active")
    public PostingPeriodPolicyEntity getActive(
            @RequestParam String companyCode,
            @RequestParam LocalDate date
    ) {
        return service.getEffectivePolicy(companyCode, date);
    }
}
