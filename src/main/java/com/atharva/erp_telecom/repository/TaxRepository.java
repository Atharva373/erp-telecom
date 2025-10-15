package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxRepository extends JpaRepository<Tax,Long> {
    Optional<Tax> findByTaxCode(String taxCode);
    Boolean existsByTaxCode(String taxCode);
}
