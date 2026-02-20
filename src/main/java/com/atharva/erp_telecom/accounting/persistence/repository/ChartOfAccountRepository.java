package com.atharva.erp_telecom.accounting.persistence.repository;

import com.atharva.erp_telecom.accounting.persistence.masterdata.ChartOfAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccountEntity,Long> {
    Optional<ChartOfAccountEntity> findByAccountCode(String accountCode);
}
