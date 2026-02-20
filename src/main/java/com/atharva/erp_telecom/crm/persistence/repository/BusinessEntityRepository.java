package com.atharva.erp_telecom.crm.persistence.repository;


import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessEntityRepository extends JpaRepository<BusinessEntity,Long> {

}
