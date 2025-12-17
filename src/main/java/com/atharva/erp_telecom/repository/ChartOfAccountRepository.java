package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount,Long> {
    Optional<ChartOfAccount> findByAccountCode(String accountCode);
}
