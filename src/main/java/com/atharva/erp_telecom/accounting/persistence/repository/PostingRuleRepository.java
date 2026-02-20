package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingRuleEntity;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostingRuleRepository extends JpaRepository<PostingRuleEntity,Long> {
    @Query("""
        SELECT r FROM PostingRule r
        WHERE r.eventType = :eventType
          AND r.companyEntity.companyId = :companyId
          AND r.active = TRUE
          AND r.effectiveFrom <= :postingDate
          AND (r.effectiveTo IS NULL OR r.effectiveTo >= :postingDate)
    """)
    Optional<PostingRuleEntity> findActiveRuleForCompany(AccountingEventType eventType, Long companyId, LocalDateTime postingDate);

    @Query("""
    SELECT r FROM PostingRule r
        WHERE r.eventType = :eventType
          AND r.companyEntity IS NULL
          AND r.active = TRUE
          AND r.effectiveFrom <= :postingDate
          AND (r.effectiveTo IS NULL OR r.effectiveTo >= :postingDate)
    """)
    Optional<PostingRuleEntity> findActiveGlobalRule(AccountingEventType eventType, LocalDateTime postingDate);
}
