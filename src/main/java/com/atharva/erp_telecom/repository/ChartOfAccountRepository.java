package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount,Long> {
    ChartOfAccount findByAccountCode(String accountCode);
}
