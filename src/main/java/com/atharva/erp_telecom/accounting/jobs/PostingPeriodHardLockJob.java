package com.atharva.erp_telecom.accounting.jobs;

import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class PostingPeriodHardLockJob {

    private final PostingPeriodRepository repository;

    public PostingPeriodHardLockJob(PostingPeriodRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 2 * * *") // daily 2 AM
    @Transactional
    public void hardLockPeriods() {
        repository.findEligibleForHardLock(LocalDateTime.now().toLocalDate())
                .forEach(period -> period.setStatus(PeriodStatus.LOCKED));
    }
}
