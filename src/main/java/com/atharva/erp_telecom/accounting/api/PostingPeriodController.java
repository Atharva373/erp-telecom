package com.atharva.erp_telecom.accounting.api;

import com.atharva.erp_telecom.accounting.dto.PostingPeriodRequest;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodRepository;
import com.atharva.erp_telecom.accounting.service.PostingPeriodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/finance/accounting/posting-periods")
@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
public class PostingPeriodController {

    private final PostingPeriodService service;
    private final PostingPeriodRepository repository;

    public PostingPeriodController(
            PostingPeriodService service,
            PostingPeriodRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /* ==============================
       CREATE
       ============================== */

    @PostMapping
    public ResponseEntity<PostingPeriodEntity> create(
            @RequestBody PostingPeriodRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    /* ==============================
       STATE TRANSITIONS
       ============================== */

    @PostMapping("/{id}/close")
    public ResponseEntity<?> close(@PathVariable Long id) {
        service.close(id, "SYSTEM");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/open")
    public ResponseEntity<?> open(@PathVariable Long id) {
        service.open(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/lock")
    public ResponseEntity<?> lock(@PathVariable Long id) {
        service.lock(id, "SYSTEM");
        return ResponseEntity.ok().build();
    }

    /* ==============================
       READ
       ============================== */

    @GetMapping
    public ResponseEntity<List<PostingPeriodEntity>> getAll(
            @RequestParam String companyCode) {

        return ResponseEntity.ok(service.getAll(companyCode));
    }
}
