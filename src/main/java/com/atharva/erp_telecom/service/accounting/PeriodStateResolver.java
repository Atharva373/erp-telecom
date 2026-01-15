package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.entity.accounting.PostingPeriodPolicy;
import com.atharva.erp_telecom.enums.PeriodStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PeriodStateResolver{

    public PeriodStatus resolve(
            PostingPeriod period,
            PostingPeriodPolicy policy,
            LocalDateTime now
    ) {
        LocalDateTime start = period.getPeriodStart().toLocalDate().atStartOfDay();
        LocalDateTime end   = period.getPeriodEnd().toLocalDate().atTime(23, 59, 59);

        // HARD LOCK
        if (now.isAfter(end.plusDays(policy.getHardLockAfterDays()))) {
            return PeriodStatus.LOCKED;
        }

        // SOFT CLOSE
        if (now.isAfter(end.plusMinutes(policy.getSoftCloseAfterMinutes()))) {
            return PeriodStatus.CLOSED;
        }

        // BUFFER WINDOW
        if (now.isAfter(end.minusMinutes(policy.getBufferBeforeEndMinutes())) ||
                now.isBefore(start.plusMinutes(policy.getBufferAfterStartMinutes()))) {
            return PeriodStatus.BUFFER_LOCKED;
        }

        return PeriodStatus.OPEN;
    }
}

