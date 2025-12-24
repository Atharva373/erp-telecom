package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.service.accounting.RevenueRecognitionExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/jobs/rev-recog")
@PreAuthorize("hasAnyRole('ADMIN','FINANCE')")
public class RevenueRecognitionJobController {

    private final RevenueRecognitionExecutor executor;

    public RevenueRecognitionJobController(
            RevenueRecognitionExecutor executor) {
        this.executor = executor;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runNow(
            @RequestParam(required = false) LocalDate runDate) {

        LocalDate effective =
                runDate != null ? runDate : LocalDate.now();

        executor.run(effective);

        return ResponseEntity.ok(
                "RevRec executed for period: " + YearMonth.from(effective)
        );
    }
}
