package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.PostingRule;
import com.atharva.erp_telecom.enums.AccountingEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostingRuleRepository extends JpaRepository<PostingRule,Long> {
    @Query("""
        SELECT r FROM PostingRule r
        WHERE r.eventType = :eventType
          AND r.company.companyId = :companyId
          AND r.active = TRUE
          AND r.effectiveFrom <= :postingDate
          AND (r.effectiveTo IS NULL OR r.effectiveTo >= :postingDate)
    """)
    Optional<PostingRule> findActiveRuleForCompany(AccountingEventType eventType, Long companyId, LocalDateTime postingDate);

    @Query("""
    SELECT r FROM PostingRule r
        WHERE r.eventType = :eventType
          AND r.company IS NULL
          AND r.active = TRUE
          AND r.effectiveFrom <= :postingDate
          AND (r.effectiveTo IS NULL OR r.effectiveTo >= :postingDate)
    """)
    Optional<PostingRule> findActiveGlobalRule(AccountingEventType eventType, LocalDateTime postingDate);
}
