package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.dto.RevenueRecognitionJobRunStatusDTO;
import com.atharva.erp_telecom.accounting.persistence.config.RevenueScheduleEntity;
import com.atharva.erp_telecom.accounting.persistence.repository.RevenueScheduleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RevenueRecognitionService {

    private final RevenueScheduleRepository repository;

    public RevenueRecognitionService(RevenueScheduleRepository repository) {
        this.repository = repository;
    }


    // Assisting methods for Revenue-recognition job
    public BigDecimal calculateMonthlyAmount(RevenueScheduleEntity schedule, YearMonth period) {
        long totalMonths =
                ChronoUnit.MONTHS.between(
                        YearMonth.from(schedule.getStartDate()),
                        YearMonth.from(schedule.getEndDate()).plusMonths(1)
                );

        BigDecimal monthly =
                schedule.getTotalAmount()
                        .divide(BigDecimal.valueOf(totalMonths), RoundingMode.HALF_UP);

        return monthly.min(schedule.getRemainingAmount());
    }

    // For reporting --> Getting the status of the Rev-rec Job run
    public List<RevenueRecognitionJobRunStatusDTO> getStatus(String companyCode) {
        return repository.findRevRecStatus(companyCode)
                .stream()
                .map(this::map)
                .toList();
    }

    private RevenueRecognitionJobRunStatusDTO map(RevenueScheduleEntity rs) {
        RevenueRecognitionJobRunStatusDTO dto = new RevenueRecognitionJobRunStatusDTO();
        dto.setScheduleId(rs.getId());
        dto.setSourceType(rs.getSourceType());
        dto.setSourceId(rs.getSourceId());
        dto.setRecognitionType(rs.getRecognitionType());
        dto.setTotalAmount(rs.getTotalAmount());
        dto.setRecognizedAmount(rs.getRecognizedAmount());
        dto.setRemainingAmount(rs.getRemainingAmount());
        dto.setLastRecognizedPeriod(rs.getLastRecognizedPeriod());
        dto.setStatus(rs.getStatus());
        return dto;
    }

}

