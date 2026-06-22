package com.atharva.erp_telecom.salesorder.persistence.repository;

import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.salesorder.persistence.transactional.Contract;
import com.atharva.erp_telecom.salesorder.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Long> {
    List<Contract> findByBusinessEntity(BusinessEntity businessEntity);
    List<Contract> findByContractStatus(ContractStatus contractStatus);
}
