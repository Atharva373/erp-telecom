package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.PostingContext;
import com.atharva.erp_telecom.entity.accounting.RevenueSchedule;
import com.atharva.erp_telecom.enums.*;
import com.atharva.erp_telecom.repository.accounting.RevenueScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class RevenueRecognitionExecutor {

    private final RevenueScheduleRepository repo;
    private final RevenueRecognitionService calc;
    private final PostingEngine postingEngine;
    private final PostingPeriodService periodService;

    public RevenueRecognitionExecutor(
            RevenueScheduleRepository repo,
            RevenueRecognitionService calc,
            PostingEngine postingEngine,
            PostingPeriodService periodService) {
        this.repo = repo;
        this.calc = calc;
        this.postingEngine = postingEngine;
        this.periodService = periodService;
    }

    @Transactional
    public void run(LocalDate runDate) {

        YearMonth period = YearMonth.from(runDate);


        // Fetch eligible schedules
        List<RevenueSchedule> schedules =
                repo.findEligibleSchedules(runDate, period);

        for (RevenueSchedule rs : schedules) {

            periodService.assertOpen(rs.getCompanyCode(),period.getYear(),period.getMonthValue());

            BigDecimal amount =
                    calc.calculateMonthlyAmount(rs, period);

            if (amount.signum() <= 0) continue;

            //  Build PostingContext
            PostingContext ctx = new PostingContext();
            ctx.setCompanyCode(rs.getCompanyCode());
            ctx.setEventType(
                    rs.getRecognitionType() == RevenueRecognitionType.DEFERRED
                            ? AccountingEventType.REVREC_DEFERRED
                            : AccountingEventType.REVREC_ACCRUED
            );
            ctx.putVar("RECOG_AMOUNT", amount);

            // Post
            postingEngine.post(ctx);

            // Update schedule (idempotency)
            rs.setRecognizedAmount(
                    rs.getRecognizedAmount().add(amount)
            );
            rs.setRemainingAmount(
                    rs.getRemainingAmount().subtract(amount)
            );
            rs.setLastRecognizedPeriod(period);

            if (rs.getRemainingAmount().signum() == 0) {
                rs.setStatus(RevenueScheduleStatus.COMPLETED);
            }

            repo.save(rs);
        }
    }
}
