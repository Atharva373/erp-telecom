package com.atharva.erp_telecom.accounting.jobs;

import com.atharva.erp_telecom.accounting.service.PostingPeriodService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class PostingPeriodScheduler {

    private final PostingPeriodService service;

    public PostingPeriodScheduler(PostingPeriodService service) {
        this.service = service;
    }

    /** Runs daily at 00:05 */
    @Scheduled(cron = "0 5 0 * * ?")
    public void autoClosePreviousPeriods() {
        // Soft-close logic can be added here
        // NEVER auto-lock
    }
}

