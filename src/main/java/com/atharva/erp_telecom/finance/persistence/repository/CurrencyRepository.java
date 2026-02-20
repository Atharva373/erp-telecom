package com.atharva.erp_telecom.finance.persistence.repository;

import com.atharva.erp_telecom.finance.persistence.masterdata.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

    Optional<CurrencyEntity> findByCurrencyCodeAndActiveTrue(String currencyCode);
}
