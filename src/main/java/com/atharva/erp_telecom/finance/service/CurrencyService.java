package com.atharva.erp_telecom.finance.service;

import com.atharva.erp_telecom.finance.persistence.masterdata.CurrencyEntity;
import com.atharva.erp_telecom.finance.persistence.repository.CurrencyRepository;
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
    public List<CurrencyEntity> create(List<CurrencyEntity> currencies) {

        if (currencies == null || currencies.isEmpty()) {
            throw new IllegalArgumentException("CurrencyEntity list cannot be empty");
        }

        // Validate all first (atomic behavior)
        for (CurrencyEntity currencyEntity : currencies) {
            validate(currencyEntity);
            if (currencyRepository.existsById(currencyEntity.getCurrencyCode())) {
                throw new IllegalArgumentException(
                        "CurrencyEntity already exists: " + currencyEntity.getCurrencyCode()
                );
            }
        }

        return currencyRepository.saveAll(currencies);
    }

    public CurrencyEntity getActive(String currencyCode) {
        return currencyRepository
                .findByCurrencyCodeAndActiveTrue(currencyCode)
                .orElseThrow(() ->
                        new IllegalArgumentException("Inactive or unknown currency: " + currencyCode)
                );
    }

    public List<CurrencyEntity> getAllActive() {
        return currencyRepository.findAll()
                .stream()
                .filter(CurrencyEntity::isActive)
                .toList();
    }

    private void validate(CurrencyEntity currencyEntity) {
        if (currencyEntity.getCurrencyCode() == null || currencyEntity.getCurrencyCode().length() != 3) {
            throw new IllegalArgumentException("Invalid currencyEntity code");
        }
        if (currencyEntity.getCurrencyName() == null || currencyEntity.getCurrencyName().isBlank()) {
            throw new IllegalArgumentException("CurrencyEntity name is required");
        }
        if (currencyEntity.getDecimalPlaces() == null || currencyEntity.getDecimalPlaces() < 0) {
            throw new IllegalArgumentException("Invalid decimal places");
        }
    }
}

