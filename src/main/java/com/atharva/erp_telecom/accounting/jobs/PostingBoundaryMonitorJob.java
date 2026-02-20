package com.atharva.erp_telecom.accounting.jobs;

import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingPeriodPolicyEntity;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodRepository;
import com.atharva.erp_telecom.accounting.service.PeriodStateResolver;
import com.atharva.erp_telecom.accounting.service.PostingPeriodPolicyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostingBoundaryMonitorJob {

    private final PostingPeriodRepository periodRepo;
    private final PostingPeriodPolicyService policyService;
    private final PeriodStateResolver stateResolver;

    public PostingBoundaryMonitorJob(PostingPeriodRepository periodRepo, PostingPeriodPolicyService policyService, PeriodStateResolver stateResolver) {
        this.periodRepo = periodRepo;
        this.policyService = policyService;
        this.stateResolver = stateResolver;
    }

    @Scheduled(cron = "0 * * * * *") // every minute
    public void monitorBoundaries() {
        LocalDateTime now = LocalDateTime.now();

        List<PostingPeriodEntity> activePeriods
                = periodRepo.findRelevantPeriods(LocalDateTime.now());

        for (PostingPeriodEntity period : activePeriods) {
            PostingPeriodPolicyEntity policy =
                    policyService.getEffectivePolicy(
                            period.getCompanyCode(),
                            now.toLocalDate()
                    );

            PeriodStatus state =
                    stateResolver.resolve(period, policy, now);

            // Optional: log / emit metric / cache freeze flag
        }
    }
}
