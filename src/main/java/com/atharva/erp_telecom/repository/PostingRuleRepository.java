package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.PostingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRuleRepository extends JpaRepository<PostingRule,Long> {
}
