package com.atharva.erp_telecom.accounting.jobs;

import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodRepository;
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
        List<PostingPeriodEntity> toClose =
                repository.findEligibleForSoftClose(LocalDate.now());

        for (PostingPeriodEntity period : toClose) {
            period.setStatus(PeriodStatus.CLOSED); // optional flag for reporting
        }
    }
}

