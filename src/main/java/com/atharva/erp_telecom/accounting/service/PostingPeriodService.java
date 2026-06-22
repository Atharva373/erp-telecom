package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.dto.PostingPeriodRequest;
import com.atharva.erp_telecom.accounting.dto.PostingPeriodResponse;
import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.accounting.persistence.repository.PostingPeriodRepository;
import com.atharva.erp_telecom.accounting.mapper.AccountingMappers;
import com.atharva.erp_telecom.exception.custom_exceptions.IllegalPostingRuleException;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.finance.persistence.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostingPeriodService {

    private final PostingPeriodRepository postingPeriodRepository;
    private final CompanyRepository companyRepository;
    private final AccountingMappers mappers;

    @Autowired
    public PostingPeriodService(PostingPeriodRepository postingPeriodRepository, CompanyRepository companyRepository, AccountingMappers mappers) {
        this.postingPeriodRepository = postingPeriodRepository;
        this.companyRepository = companyRepository;
        this.mappers = mappers;
    }

    /* ==============================
       CORE VALIDATION
       ============================== */

    public boolean isOpen(String companyCode, LocalDate postingDate) {

        int fiscalYear = postingDate.getYear();
        int period = postingDate.getMonthValue();

        PostingPeriodEntity pp = postingPeriodRepository
                .findByCompany_CompanyCodeAndFiscalYearAndPostingPeriod(
                        companyCode, fiscalYear, period
                )
                .orElseThrow(() ->
                        new IllegalStateException("Posting period not configured: " + fiscalYear + "-" + period)
                );

        return pp.getStatus() == PeriodStatus.OPEN;
    }

    /* ==============================
       CREATION
       ============================== */

    @Transactional
    public PostingPeriodEntity create(PostingPeriodRequest req) {

//        postingPeriodRepository.findByCompany_CompanyCodeAndFiscalYearAndPostingPeriod(
//                req.getCompanyCode(),
//                req.getFiscalYear(),
//                req.getPostingPeriod()
//        ).ifPresent(pp -> {
//            throw new IllegalStateException("Posting period already exists with current combination of CompanyCode, FiscalYear and PostingPeriodEntity !");
//        });


        CompanyEntity company = companyRepository.findByCompanyCode(req.getCompanyCode()).orElseThrow(() -> new RuntimeException("Company NOT found"));
        PostingPeriodEntity period = mappers.toPostingPeriodEntity(req,company);
        return postingPeriodRepository.save(period);
    }

    /* ==============================
       PERIOD STATE TRANSITIONS
       ============================== */

    @Transactional
    public void close(Long periodId, String user) {

        PostingPeriodEntity period = get(periodId);

        if (period.getStatus() != PeriodStatus.OPEN) {
            throw new IllegalStateException("Only OPEN periods can be closed");
        }

        period.setStatus(PeriodStatus.CLOSED);
        period.setClosedOn(LocalDateTime.now());
        period.setClosedBy(user);
    }

    @Transactional
    public void open(Long periodId) {

        PostingPeriodEntity period = get(periodId);

        if (period.getStatus() == PeriodStatus.LOCKED) {
            throw new IllegalStateException("LOCKED period cannot be reopened");
        }

        period.setStatus(PeriodStatus.OPEN);
        period.setClosedOn(null);
        period.setClosedBy(null);
    }

    @Transactional
    public void lock(Long periodId, String user) {

        PostingPeriodEntity period = get(periodId);

        if (period.getStatus() != PeriodStatus.CLOSED) {
            throw new IllegalStateException("Only CLOSED periods can be locked");
        }

        period.setStatus(PeriodStatus.LOCKED);
        period.setClosedOn(LocalDateTime.now());
        period.setClosedBy(user);
    }

    /* ==============================
       QUERY HELPERS
       ============================== */

    public PostingPeriodEntity get(Long id) {
        return postingPeriodRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Posting period not found")
                );
    }

    public List<PostingPeriodResponse> getAll(String companyCode) {
        List<PostingPeriodEntity> returnedPeriods = postingPeriodRepository.findByCompany_CompanyCodeAndFiscalYear(
                companyCode, LocalDate.now().getYear()
        );
        return returnedPeriods.stream()
                .map(mappers::toPostingPeriodResponse).toList();
    }

    public PostingPeriodEntity getRequiredOpenPeriod(String companyCode,LocalDate eventDate){

        PostingPeriodEntity period = postingPeriodRepository.getRequiredPeriod(companyCode,eventDate);
        if (period == null) {
            throw new IllegalPostingRuleException("Posting period not configured for company=" + companyCode + ", date=" + eventDate);
        }

        if (period.getStatus() != PeriodStatus.OPEN) {
            throw new IllegalPostingRuleException("Posting period is closed for company=" + companyCode + ", period=" + period.getPostingPeriod());
        }
        return period;
    }

    public void assertOpen(String companyCode,int year,int period) {
        PostingPeriodEntity pp = load(companyCode, year, period);
        if (pp.getStatus() != PeriodStatus.OPEN) {
            throw new IllegalStateException("Posting period not OPEN: " + year + "-" + period);
        }
    }

    // Additional logic for 13th Posting Period --> Adjustment period.
    public void assertOpenOrAdjustment(
            String companyCode,
            int year,
            int period
    ) {
        PostingPeriodEntity pp = load(companyCode, year, period);

        if (pp.getStatus() == PeriodStatus.LOCKED) {
            throw new IllegalStateException(
                    "Posting period LOCKED: " + year + "-" + period
            );
        }

        // CLOSED normal periods blocked, but adjustment allowed
        if (pp.getStatus() == PeriodStatus.CLOSED && period != 13) {
            throw new IllegalStateException(
                    "Posting period CLOSED (non-adjustment): " + year + "-" + period
            );
        }
    }

    private PostingPeriodEntity load(String companyCode, int year, int period) {
        return postingPeriodRepository.findByCompany_CompanyCodeAndFiscalYearAndPostingPeriod(companyCode, year, period)
                .orElseThrow(() ->
                        new IllegalStateException("Posting period not configured"));
    }
}

