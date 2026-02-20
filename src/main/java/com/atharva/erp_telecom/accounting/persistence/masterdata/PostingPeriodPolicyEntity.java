package com.atharva.erp_telecom.accounting.persistence.masterdata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "posting_period_policy")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class PostingPeriodPolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyCode;

    // Buffer windows:
    /*
        1. BUFFER BEFORE (Freeze postings BEFORE the period ends)

        EG: Period end: Jan 31 23:59    |   bufferBeforeEndMinutes = 5  | At 23:54 → POSTING FREEZE STARTS

        PURPOSE:
        Because at month end:
        - Massive posting spikes
        - Batch jobs running
        - Payments, invoices, reversals
        - Long-running DB transactions

        Without buffer-before:

        ❌ A posting starts at 23:59:59
        ❌ Commits at 00:00:04
        ❌ Lands in wrong logical period

        💥 Ledger corruption.

        ================================================================================================================

        2. BUFFER AFTER (Delay postings AFTER the new period starts)

        EG: New period start: Feb 1 00:00   |   bufferAfterStartMinutes = 10    | From 00:00 → 00:10 → POSTING FREEZE

        PURPOSE:
        Because right after period switch:
        - Balances are being carried forward
        - Period close validations running
        - Snapshot jobs executing
        - Opening balance journals may be posting

        Allowing postings immediately can cause:

        ❌ Balance mismatches
        ❌ Reports pulling partial data
        ❌ Opening balances out of sync

     */
    private Integer bufferBeforeEndMinutes = 5; // Postings stop at this time before the End time to stop wrong posting timing.
    private Integer bufferAfterStartMinutes = 10;    // Postings don't start .

    // Close rules
    private Integer softCloseAfterMinutes = 0;  // Posting State --> Closed (Postings allowed only by reversals or adjustments).
    private Integer hardLockAfterDays = 7;  // Posting State --> Locked (No further postings).

    // Controls
    private Boolean allowBackdatedPosting = false;
    private Boolean allowFuturePosting = false;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    @CreatedDate
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

    private String createdBy;
    private String modifiedBy;
}
