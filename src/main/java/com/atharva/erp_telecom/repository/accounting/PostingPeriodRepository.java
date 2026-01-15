package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.enums.PeriodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostingPeriodRepository extends JpaRepository<PostingPeriod, Long> {

    Optional<PostingPeriod> findByCompanyCodeAndFiscalYearAndPostingPeriod(
            String companyCode,
            Integer fiscalYear,
            Integer postingPeriod
    );

    List<PostingPeriod> findByCompanyCodeAndFiscalYear(
            String companyCode,
            Integer fiscalYear
    );

    List<PostingPeriod> findByCompanyCodeAndStatus(
            String companyCode,
            PeriodStatus status
    );

    @Query("""
    SELECT p FROM PostingPeriod p
    WHERE p.periodStart <= :now
      AND (p.periodEnd IS NULL OR p.periodEnd >= :now)
    """)
    List<PostingPeriod> findRelevantPeriods(@Param("now") LocalDateTime now);



    @Query("""
    SELECT p FROM PostingPeriod p
    WHERE p.companyCode = :companyCode
      AND :eventDate BETWEEN p.startDate AND p.endDate
    """)
    Optional<PostingPeriod> findPeriodForDate(
            @Param("companyCode") String companyCode,
            @Param("eventDate") LocalDate eventDate
    );


    @Query("""
    SELECT p FROM PostingPeriod p
    WHERE p.startDate <= :today
      AND p.endDate >= :today
""")
    List<PostingPeriod> findActivePeriods(@Param("today") LocalDate today);


    @Query("""
    SELECT p FROM PostingPeriod p
    WHERE p.endDate < :today
      AND p.softClosed = false
""")
    List<PostingPeriod> findEligibleForSoftClose(
            @Param("today") LocalDate today
    );

    @Query("""
    SELECT p FROM PostingPeriod p
    WHERE p.endDate < :cutoffDate
      AND p.locked = false
""")
    List<PostingPeriod> findEligibleForHardLock(
            @Param("cutoffDate") LocalDate cutoffDate
    );


    @Query("""
    SELECT p FROM PostingPeriod p
    WHERE p.companyCode = :companyCode
      AND p.startDate <= :eventDate
      AND p.endDate >= :eventDate
""")
    PostingPeriod getRequiredPeriod(
            @Param("companyCode") String companyCode,
            @Param("eventDate") LocalDate eventDate
    );

    // Optional<PostingPeriod> findByCompanyIdAndFiscalYearAndPeriod(Long companyId, Integer fiscalYear, Integer period);
}
