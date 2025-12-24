package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.entity.accounting.RevenueSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface RevenueScheduleRepository
        extends JpaRepository<RevenueSchedule, Long> {

    @Query("""
        SELECT rs
        FROM RevenueSchedule rs
        WHERE rs.status = 'ACTIVE'
          AND rs.startDate <= :runDate
          AND rs.endDate >= :runDate
          AND rs.remainingAmount > 0
          AND (rs.lastRecognizedPeriod IS NULL
               OR rs.lastRecognizedPeriod < :period)
    """)
    List<RevenueSchedule> findEligibleSchedules(
            LocalDate runDate,
            YearMonth period
    );

    @Query("""
        SELECT rs
        FROM RevenueSchedule rs
        WHERE rs.companyId = :companyId
        ORDER BY rs.lastRecognizedPeriod DESC NULLS LAST
    """)
    List<RevenueSchedule> findRevRecStatus(Long companyId);
}
