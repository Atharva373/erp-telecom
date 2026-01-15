package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.entity.accounting.PostingPeriodPolicy;
import com.atharva.erp_telecom.repository.accounting.PostingPeriodPolicyRepository;
import com.atharva.erp_telecom.service.accounting.PostingPeriodPolicyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/config/finance/accounting/posting-period-policy")
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
    public PostingPeriodPolicy create(@RequestBody PostingPeriodPolicy policy) {
        // validate policy ranges if needed
        return repository.save(policy);
    }

    @GetMapping("/active")
    public PostingPeriodPolicy getActive(
            @RequestParam String companyCode,
            @RequestParam LocalDate date
    ) {
        return service.getEffectivePolicy(companyCode, date);
    }
}
