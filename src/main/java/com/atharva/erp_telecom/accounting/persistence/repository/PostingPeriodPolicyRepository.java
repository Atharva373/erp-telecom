package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface PostingPeriodPolicyRepository extends JpaRepository<PostingPeriodPolicyEntity,Long> {
    @Query("""
    SELECT p FROM PostingPeriodPolicyEntity p
    WHERE p.companyCode = :companyCode
      AND :date BETWEEN p.effectiveFrom AND COALESCE(p.effectiveTo, :date)
""")
    Optional<PostingPeriodPolicyEntity> findEffectivePolicy(
            String companyCode,
            LocalDate date
    );

}
