package com.atharva.erp_telecom.finance.api;

import com.atharva.erp_telecom.finance.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.atharva.erp_telecom.finance.persistence.masterdata.CurrencyEntity;

import java.util.List;

@RestController
@RequestMapping("/masterdata/finance/accounting/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
    @PostMapping
    public ResponseEntity<List<CurrencyEntity>> create(@RequestBody List<CurrencyEntity> currencies) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(currencyService.create(currencies));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','USER')")
    @GetMapping("/{code}")
    public ResponseEntity<CurrencyEntity> get(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getActive(code));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','USER')")
    @GetMapping
    public ResponseEntity<List<CurrencyEntity>> getAll() {
        return ResponseEntity.ok(currencyService.getAllActive());
    }
}
