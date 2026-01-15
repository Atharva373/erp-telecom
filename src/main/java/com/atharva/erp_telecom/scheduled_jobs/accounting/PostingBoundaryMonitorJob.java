package com.atharva.erp_telecom.scheduled_jobs.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.entity.accounting.PostingPeriodPolicy;
import com.atharva.erp_telecom.enums.PeriodStatus;
import com.atharva.erp_telecom.exception.custom_exceptions.PostingPeriodException;
import com.atharva.erp_telecom.repository.accounting.PostingPeriodRepository;
import com.atharva.erp_telecom.service.accounting.PeriodStateResolver;
import com.atharva.erp_telecom.service.accounting.PostingPeriodPolicyService;
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

        List<PostingPeriod> activePeriods
                = periodRepo.findRelevantPeriods(LocalDateTime.now());

        for (PostingPeriod period : activePeriods) {
            PostingPeriodPolicy policy =
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
