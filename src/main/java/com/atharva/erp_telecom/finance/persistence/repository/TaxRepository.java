package com.atharva.erp_telecom.finance.persistence.repository;

import com.atharva.erp_telecom.finance.persistence.masterdata.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxRepository extends JpaRepository<TaxEntity,Long> {
    Optional<TaxEntity> findByTaxCode(String taxCode);
    Boolean existsByTaxCode(String taxCode);
}
