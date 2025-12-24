package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.dto.BalanceSheetResponse;
import com.atharva.erp_telecom.dto.GLLineRow;
import com.atharva.erp_telecom.dto.ProfitAndLossResponse;
import com.atharva.erp_telecom.dto.TrialBalanceRow;
import com.atharva.erp_telecom.service.BalanceSheetService;
import com.atharva.erp_telecom.service.GeneralLedgerService;
import com.atharva.erp_telecom.service.ProfitAndLossService;
import com.atharva.erp_telecom.service.TrialBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN','FINANCE','AUDITOR')")
public class FinancialReportingController {

    private final TrialBalanceService trialBalanceService;
    private final GeneralLedgerService generalLedgerService;
    private final BalanceSheetService balanceSheetService;
    private final ProfitAndLossService profitAndLossService;

    public FinancialReportingController(TrialBalanceService trialBalanceService, GeneralLedgerService generalLedgerService, BalanceSheetService balanceSheetService, ProfitAndLossService profitAndLossService) {
        this.trialBalanceService = trialBalanceService;
        this.generalLedgerService = generalLedgerService;
        this.balanceSheetService = balanceSheetService;
        this.profitAndLossService = profitAndLossService;
    }

    /**
     * ENDPOINT FOR GENERATING REPORT FOR GETTING THE TRIAL BALANCE FOR A PARTICULAR COMPANY (Financial Entity).
     *
     * @param companyId
     * @param fiscalYear
     * @param postingPeriod
     * @param currency
     * @return
     */
    @GetMapping("/trial-balance")
    public ResponseEntity<List<TrialBalanceRow>> trialBalance(
            @RequestParam Long companyId,
            @RequestParam Integer fiscalYear,
            @RequestParam Integer postingPeriod,
            @RequestParam String currency
    ) {
        return ResponseEntity.ok(
                trialBalanceService.getTrialBalance(companyId, fiscalYear, postingPeriod, currency)
        );
    }


    /**
     * ENDPOINT FOR GENERATING REPORT FOR A GENERAL LEDGER SNAPSHOT FOR A PARTICULAR ACCOUNT CODE
     *
     * @param accountCode
     * @param companyId
     * @param fiscalYear
     * @param postingPeriod
     * @param currency
     * @return ResponseEntity<List<GLLineRow>>
     */
    @GetMapping("/gl/{accountCode}")
    public ResponseEntity<List<GLLineRow>> gl(
            @PathVariable String accountCode,
            @RequestParam Long companyId,
            @RequestParam Integer fiscalYear,
            @RequestParam Integer postingPeriod,
            @RequestParam String currency
    ) {
        return ResponseEntity.ok(
                generalLedgerService.getGL(companyId, accountCode, fiscalYear, postingPeriod, currency)
        );
    }

    /**
     * Balance Sheet answers:
     * “What do we own and owe at a point in time?”
     *
     * @param companyId
     * @param fiscalYear
     * @param postingPeriod
     * @param currency
     * @return
     */
    @GetMapping("/balance-sheet")
    public BalanceSheetResponse balanceSheet(
            @RequestParam Long companyId,
            @RequestParam Integer fiscalYear,
            @RequestParam Integer postingPeriod,
            @RequestParam String currency
    ) {
        return balanceSheetService.generate(
                companyId, fiscalYear, postingPeriod, currency
        );
    }

    /**
     * Profit & Loss answers:
     * “How did we perform over a period of time?”
     * @param companyId
     * @param fiscalYear
     * @param fromPeriod
     * @param toPeriod
     * @param currency
     * @return
     */
    @GetMapping("/profit-loss")
    public ProfitAndLossResponse pnl(
            @RequestParam Long companyId,
            @RequestParam Integer fiscalYear,
            @RequestParam Integer fromPeriod,
            @RequestParam Integer toPeriod,
            @RequestParam String currency
    ) {
        return profitAndLossService.generate(
                companyId, fiscalYear, fromPeriod, toPeriod, currency
        );
    }

}

