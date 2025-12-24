package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.entity.accounting.Currency;
import com.atharva.erp_telecom.repository.accounting.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    @Transactional
    public List<Currency> create(List<Currency> currencies) {

        if (currencies == null || currencies.isEmpty()) {
            throw new IllegalArgumentException("Currency list cannot be empty");
        }

        // Validate all first (atomic behavior)
        for (Currency currency : currencies) {
            validate(currency);
            if (currencyRepository.existsById(currency.getCurrencyCode())) {
                throw new IllegalArgumentException(
                        "Currency already exists: " + currency.getCurrencyCode()
                );
            }
        }

        return currencyRepository.saveAll(currencies);
    }

    public Currency getActive(String currencyCode) {
        return currencyRepository
                .findByCurrencyCodeAndActiveTrue(currencyCode)
                .orElseThrow(() ->
                        new IllegalArgumentException("Inactive or unknown currency: " + currencyCode)
                );
    }

    public List<Currency> getAllActive() {
        return currencyRepository.findAll()
                .stream()
                .filter(Currency::isActive)
                .toList();
    }

    private void validate(Currency currency) {
        if (currency.getCurrencyCode() == null || currency.getCurrencyCode().length() != 3) {
            throw new IllegalArgumentException("Invalid currency code");
        }
        if (currency.getCurrencyName() == null || currency.getCurrencyName().isBlank()) {
            throw new IllegalArgumentException("Currency name is required");
        }
        if (currency.getDecimalPlaces() == null || currency.getDecimalPlaces() < 0) {
            throw new IllegalArgumentException("Invalid decimal places");
        }
    }
}

