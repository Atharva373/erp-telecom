package com.atharva.erp_telecom.repository.salesorder;

import com.atharva.erp_telecom.entity.salesorder.Contract;
import com.atharva.erp_telecom.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Long> {
    List<Contract> findByCustomerCustomerId(Long customerId);
    List<Contract> findByContractStatus(ContractStatus contractStatus);
}
