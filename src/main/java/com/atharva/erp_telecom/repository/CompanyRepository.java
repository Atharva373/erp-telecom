package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    List<Company> findByParentCompany(Company parent);
    boolean existsByCompanyCode(String companyCode);
    Company findByStateCode(String stateCode);
}
