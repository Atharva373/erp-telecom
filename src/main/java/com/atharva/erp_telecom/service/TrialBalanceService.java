package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.dto.TrialBalanceRow;
import com.atharva.erp_telecom.repository.TrialBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrialBalanceService {

    private final TrialBalanceRepository repo;

    public TrialBalanceService(TrialBalanceRepository repo) {
        this.repo = repo;
    }

    public List<TrialBalanceRow> getTrialBalance(
            Long companyId,
            Integer fiscalYear,
            Integer postingPeriod,
            String currency
    ) {
        return repo.fetchTrialBalance(
                companyId, fiscalYear, postingPeriod, currency
        );
    }
}
