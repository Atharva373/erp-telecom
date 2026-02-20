package com.atharva.erp_telecom.accounting.jobs;

import com.atharva.erp_telecom.accounting.service.RevenueRecognitionExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@EnableScheduling
public class RevenueRecognitionScheduler {

    private final RevenueRecognitionExecutor executor;

    @Value("${jobs.revrec.enabled:true}")
    private boolean enabled=false;

    public RevenueRecognitionScheduler(
            RevenueRecognitionExecutor executor) {
        this.executor = executor;
    }

    @Scheduled(cron = "${jobs.revrec.cron}")
    public void scheduledRun() {
        if (!enabled) return;
        executor.run(LocalDate.now());
    }
}
