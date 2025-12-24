package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.PostingPeriod;
import com.atharva.erp_telecom.enums.PeriodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostingPeriodRepository extends JpaRepository<PostingPeriod, Long> {

    Optional<PostingPeriod> findByCompanyIdAndFiscalYearAndPostingPeriod(
            Long companyId,
            Integer fiscalYear,
            Integer postingPeriod
    );

    List<PostingPeriod> findByCompanyIdAndFiscalYear(
            Long companyId,
            Integer fiscalYear
    );

    List<PostingPeriod> findByCompanyIdAndStatus(
            Long companyId,
            PeriodStatus status
    );
}
