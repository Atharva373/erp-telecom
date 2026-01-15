package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.TrialBalanceRow;
import com.atharva.erp_telecom.repository.accounting.TrialBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrialBalanceService {

    private final TrialBalanceRepository repo;

    @Autowired
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
