package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.atharva.erp_telecom.entity.Currency;

import java.util.List;

@RestController
@RequestMapping("/config/finance/accounting/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
    @PostMapping
    public ResponseEntity<List<Currency>> create(@RequestBody List<Currency> currencies) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(currencyService.create(currencies));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','USER')")
    @GetMapping("/{code}")
    public ResponseEntity<Currency> get(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getActive(code));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','USER')")
    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyService.getAllActive());
    }
}
