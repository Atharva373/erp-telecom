package com.atharva.erp_telecom.repository.finance;

import com.atharva.erp_telecom.entity.finance.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    List<Company> findByParentCompany(Company parent);
    boolean existsByCompanyCode(String companyCode);
    Optional<Company> findByStateCode(String stateCode);
    Optional<Company> findByCompanyCode(String companyCode);
}
