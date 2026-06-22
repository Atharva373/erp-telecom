package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.engine.PostingEngine;
import com.atharva.erp_telecom.accounting.engine.context.PostingContext;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.atharva.erp_telecom.accounting.enums.RevenueRecognitionType;
import com.atharva.erp_telecom.accounting.enums.RevenueScheduleStatus;
import com.atharva.erp_telecom.accounting.persistence.config.RevenueScheduleEntity;
import com.atharva.erp_telecom.accounting.persistence.repository.RevenueScheduleRepository;
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
        List<RevenueScheduleEntity> schedules =
                repo.findEligibleSchedules(runDate, period);

        for (RevenueScheduleEntity rs : schedules) {

            periodService.assertOpen(rs.getCompanyCode(),period.getYear(),period.getMonthValue());

            BigDecimal amount =
                    calc.calculateMonthlyAmount(rs, period);

            if (amount.signum() <= 0) continue;

            //  Build PostingContext
            PostingContext ctx = PostingContext.builder().build();
//            ctx.setCompanyCode(rs.getCompanyCode());
//            ctx.setEventType(
//                    rs.getRecognitionType() == RevenueRecognitionType.DEFERRED
//                            ? AccountingEventType.REVREC_DEFERRED
//                            : AccountingEventType.REVREC_ACCRUED
//            );
//            ctx.putVar("RECOG_AMOUNT", amount);

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
