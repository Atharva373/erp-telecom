package com.atharva.erp_telecom.finance.persistence.repository;

import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
    List<CompanyEntity> findByParentCompany(CompanyEntity parent);
    boolean existsByCompanyCode(String companyCode);
    Optional<CompanyEntity> findByStateCode(String stateCode);
    Optional<CompanyEntity> findByCompanyCode(String companyCode);
}
