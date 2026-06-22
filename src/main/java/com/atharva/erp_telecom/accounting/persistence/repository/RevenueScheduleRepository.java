package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.persistence.config.RevenueScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface RevenueScheduleRepository
        extends JpaRepository<RevenueScheduleEntity, Long> {

    @Query("""
        SELECT rs
        FROM RevenueScheduleEntity rs
        WHERE rs.status = 'ACTIVE'
          AND rs.startDate <= :runDate
          AND rs.endDate >= :runDate
          AND rs.remainingAmount > 0
          AND (rs.lastRecognizedPeriod IS NULL
               OR rs.lastRecognizedPeriod < :period)
    """)
    List<RevenueScheduleEntity> findEligibleSchedules(
            LocalDate runDate,
            YearMonth period
    );

    @Query("""
        SELECT rs
        FROM RevenueScheduleEntity rs
        WHERE rs.companyCode = :companyCode
        ORDER BY rs.lastRecognizedPeriod DESC NULLS LAST
    """)
    List<RevenueScheduleEntity> findRevRecStatus(String companyCode);
}
