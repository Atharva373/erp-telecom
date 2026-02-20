package com.atharva.erp_telecom.accounting.api;


import com.atharva.erp_telecom.accounting.dto.RevenueRecognitionJobRunStatusDTO;
import com.atharva.erp_telecom.accounting.service.RevenueRecognitionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports/revrec")
@PreAuthorize("hasAnyRole('ADMIN','FINANCE','AUDITOR')")
public class RevenueRecognitionReportingController {
    private final RevenueRecognitionService service;

    public RevenueRecognitionReportingController(RevenueRecognitionService service) {
        this.service = service;
    }

    @GetMapping("/status")
    public List<RevenueRecognitionJobRunStatusDTO> status(
            @RequestParam Long companyId
    ) {
        return service.getStatus(companyId);
    }
}
