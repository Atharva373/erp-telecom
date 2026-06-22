package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostingPeriodRepository extends JpaRepository<PostingPeriodEntity, Long> {

    Optional<PostingPeriodEntity> findByCompany_CompanyCodeAndFiscalYearAndPostingPeriod(
            String companyCode,
            Integer fiscalYear,
            Integer postingPeriod
    );

    List<PostingPeriodEntity> findByCompany_CompanyCodeAndFiscalYear(
            String companyCode,
            Integer fiscalYear
    );

    List<PostingPeriodEntity> findByCompany_CompanyCodeAndStatus(
            String companyCode,
            PeriodStatus status
    );

    @Query("""
    SELECT p FROM PostingPeriodEntity p
    WHERE p.periodStart <= :now
      AND (p.periodEnd IS NULL OR p.periodEnd >= :now)
    """)
    List<PostingPeriodEntity> findRelevantPeriods(@Param("now") LocalDateTime now);



    @Query("""
    SELECT p FROM PostingPeriodEntity p
    WHERE p.company.companyCode = :companyCode
      AND :eventDate BETWEEN p.periodStart AND p.periodEnd
    """)
    Optional<PostingPeriodEntity> findPeriodForDate(
            @Param("companyCode") String companyCode,
            @Param("eventDate") LocalDate eventDate
    );


    @Query("""
    SELECT p FROM PostingPeriodEntity p
    WHERE p.periodStart <= :today
      AND p.periodEnd >= :today
""")
    List<PostingPeriodEntity> findActivePeriods(@Param("today") LocalDate today);


    @Query("""
    SELECT p FROM PostingPeriodEntity p
    WHERE p.periodEnd < :today
""")
    List<PostingPeriodEntity> findEligibleForSoftClose(
            @Param("today") LocalDate today
    );

    @Query("""
    SELECT p FROM PostingPeriodEntity p
    WHERE p.periodEnd < :cutoffDate
""")
    List<PostingPeriodEntity> findEligibleForHardLock(
            @Param("cutoffDate") LocalDate cutoffDate
    );


    @Query("""
    SELECT p FROM PostingPeriodEntity p
    WHERE p.company.companyCode = :companyCode
      AND p.periodStart <= :eventDate
      AND p.periodEnd >= :eventDate
""")
    PostingPeriodEntity getRequiredPeriod(@Param("companyCode") String companyCode, @Param("eventDate") LocalDate eventDate);

    // Optional<PostingPeriodEntity> findByCompanyIdAndFiscalYearAndPeriod(Long companyId, Integer fiscalYear, Integer period);
}
