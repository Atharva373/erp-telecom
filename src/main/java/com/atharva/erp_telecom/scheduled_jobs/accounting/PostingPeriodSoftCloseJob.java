package com.atharva.erp_telecom.scheduled_jobs.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.enums.PeriodStatus;
import com.atharva.erp_telecom.repository.accounting.PostingPeriodRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class PostingPeriodSoftCloseJob {

    private final PostingPeriodRepository repository;

    public PostingPeriodSoftCloseJob(PostingPeriodRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void softClosePeriods() {
        List<PostingPeriod> toClose =
                repository.findEligibleForSoftClose(LocalDate.now());

        for (PostingPeriod period : toClose) {
            period.setStatus(PeriodStatus.CLOSED); // optional flag for reporting
        }
    }
}

