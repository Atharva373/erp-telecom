package com.atharva.erp_telecom.scheduled_jobs.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.enums.PeriodStatus;
import com.atharva.erp_telecom.repository.accounting.PostingPeriodRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class PostingPeriodHardLockJob {

    private final PostingPeriodRepository repository;

    public PostingPeriodHardLockJob(PostingPeriodRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 2 * * *") // daily 2 AM
    @Transactional
    public void hardLockPeriods() {
        repository.findEligibleForHardLock(LocalDate.now())
                .forEach(period -> period.setStatus(PeriodStatus.LOCKED));
    }
}
