package com.atharva.erp_telecom.repository.accounting;

import com.atharva.erp_telecom.entity.accounting.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {

    Optional<Currency> findByCurrencyCodeAndActiveTrue(String currencyCode);
}
