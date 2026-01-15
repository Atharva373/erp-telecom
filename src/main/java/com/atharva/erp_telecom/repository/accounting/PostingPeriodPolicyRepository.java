package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.entity.accounting.PostingPeriodPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface PostingPeriodPolicyRepository extends JpaRepository<PostingPeriodPolicy,Long> {
    @Query("""
    SELECT p FROM PostingPeriodPolicy p
    WHERE p.companyCode = :companyCode
      AND :date BETWEEN p.effectiveFrom AND COALESCE(p.effectiveTo, :date)
""")
    Optional<PostingPeriodPolicy> findEffectivePolicy(
            String companyCode,
            LocalDate date
    );

}
