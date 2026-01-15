package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.PostingPeriodRequest;
import com.atharva.erp_telecom.entity.accounting.PostingPeriod;
import com.atharva.erp_telecom.enums.PeriodStatus;
import com.atharva.erp_telecom.repository.accounting.PostingPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostingPeriodService {

    private final PostingPeriodRepository repository;

    @Autowired
    public PostingPeriodService(PostingPeriodRepository repository) {
        this.repository = repository;
    }

    /* ==============================
       CORE VALIDATION
       ============================== */

    public boolean isOpen(String companyCode, LocalDate postingDate) {

        int fiscalYear = postingDate.getYear();
        int period = postingDate.getMonthValue();

        PostingPeriod pp = repository
                .findByCompanyCodeAndFiscalYearAndPostingPeriod(
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
    public PostingPeriod create(PostingPeriodRequest req) {

        repository.findByCompanyCodeAndFiscalYearAndPostingPeriod(
                req.getCompanyCode(),
                req.getFiscalYear(),
                req.getPostingPeriod()
        ).ifPresent(pp -> {
            throw new IllegalStateException("Posting period already exists with current combination of CompanyCode, FiscalYear and PostingPeriod !");
        });

        PostingPeriod period = new PostingPeriod();
        period.setCompanyCode(req.getCompanyCode());
        period.setFiscalYear(req.getFiscalYear());
        period.setPostingPeriod(req.getPostingPeriod());
        period.setPeriodStart(req.getPeriodStart());
        period.setPeriodEnd(req.getPeriodEnd());
        period.setStatus(PeriodStatus.OPEN);

        return repository.save(period);
    }

    /* ==============================
       PERIOD STATE TRANSITIONS
       ============================== */

    @Transactional
    public void close(Long periodId, String user) {

        PostingPeriod period = get(periodId);

        if (period.getStatus() != PeriodStatus.OPEN) {
            throw new IllegalStateException("Only OPEN periods can be closed");
        }

        period.setStatus(PeriodStatus.CLOSED);
        period.setClosedOn(LocalDateTime.now());
        period.setClosedBy(user);
    }

    @Transactional
    public void open(Long periodId) {

        PostingPeriod period = get(periodId);

        if (period.getStatus() == PeriodStatus.LOCKED) {
            throw new IllegalStateException("LOCKED period cannot be reopened");
        }

        period.setStatus(PeriodStatus.OPEN);
        period.setClosedOn(null);
        period.setClosedBy(null);
    }

    @Transactional
    public void lock(Long periodId, String user) {

        PostingPeriod period = get(periodId);

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

    public PostingPeriod get(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Posting period not found")
                );
    }

    public List<PostingPeriod> getAll(String companyCode) {
        return repository.findByCompanyCodeAndFiscalYear(
                companyCode, LocalDate.now().getYear()
        );
    }

    public void assertOpen(
            String companyCode,
            int year,
            int period
    ) {
        PostingPeriod pp = load(companyCode, year, period);

        if (pp.getStatus() != PeriodStatus.OPEN) {
            throw new IllegalStateException(
                    "Posting period not OPEN: " + year + "-" + period
            );
        }
    }

    // Additional logic for 13th Posting Period --> Adjustment period.
    public void assertOpenOrAdjustment(
            String companyCode,
            int year,
            int period
    ) {
        PostingPeriod pp = load(companyCode, year, period);

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

    private PostingPeriod load(String companyCode, int year, int period) {
        return repository.findByCompanyCodeAndFiscalYearAndPostingPeriod(companyCode, year, period)
                .orElseThrow(() ->
                        new IllegalStateException("Posting period not configured"));
    }
}

