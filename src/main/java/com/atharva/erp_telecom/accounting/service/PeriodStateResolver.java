package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodPolicyEntity;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PeriodStateResolver{

    public PeriodStatus resolve(
            PostingPeriodEntity period,
            PostingPeriodPolicyEntity policy,
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

