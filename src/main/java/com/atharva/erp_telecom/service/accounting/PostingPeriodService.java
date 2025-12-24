package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.PostingPeriodCreateRequest;
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

    public boolean isOpen(Long companyId, LocalDate postingDate) {

        int fiscalYear = postingDate.getYear();
        int period = postingDate.getMonthValue();

        PostingPeriod pp = repository
                .findByCompanyIdAndFiscalYearAndPostingPeriod(
                        companyId, fiscalYear, period
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
    public PostingPeriod create(PostingPeriodCreateRequest req) {

        repository.findByCompanyIdAndFiscalYearAndPostingPeriod(
                req.getCompanyId(),
                req.getFiscalYear(),
                req.getPostingPeriod()
        ).ifPresent(pp -> {
            throw new IllegalStateException("Posting period already exists");
        });

        PostingPeriod period = new PostingPeriod();
        period.setCompanyId(req.getCompanyId());
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

    public List<PostingPeriod> getAll(Long companyId) {
        return repository.findByCompanyIdAndFiscalYear(
                companyId, LocalDate.now().getYear()
        );
    }

    public void assertOpen(
            Long companyId,
            int year,
            int period
    ) {
        PostingPeriod pp = load(companyId, year, period);

        if (pp.getStatus() != PeriodStatus.OPEN) {
            throw new IllegalStateException(
                    "Posting period not OPEN: " + year + "-" + period
            );
        }
    }

    // Additional logic for 13th Posting Period --> Adjustment period.
    public void assertOpenOrAdjustment(
            Long companyId,
            int year,
            int period
    ) {
        PostingPeriod pp = load(companyId, year, period);

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

    private PostingPeriod load(Long companyId, int year, int period) {
        return repository.findByCompanyIdAndFiscalYearAndPeriod(companyId, year, period)
                .orElseThrow(() ->
                        new IllegalStateException("Posting period not configured"));
    }
}

