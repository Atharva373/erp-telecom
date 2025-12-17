package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.dto.ChartOfAccountRequest;
import com.atharva.erp_telecom.dto.ChartOfAccountResponse;
import com.atharva.erp_telecom.service.ChartOfAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config/finance/accounting/chart-of-accounts")
@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
public class ChartOfAccountController {

    private final ChartOfAccountService service;

    public ChartOfAccountController(ChartOfAccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChartOfAccountResponse> create(@RequestBody ChartOfAccountRequest dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ChartOfAccountResponse>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChartOfAccountResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChartOfAccountResponse> update(
            @PathVariable Long id,
            @RequestBody ChartOfAccountRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}