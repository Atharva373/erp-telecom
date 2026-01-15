package com.atharva.erp_telecom.enums;

public enum PeriodStatus {
    OPEN,   // Open --> The period is active and available for accounting entries.
    CLOSED, // Closed --> Soft Closed - The period is restricted, but not fully closed
    LOCKED,  // Locked --> Hard Closed - The period is frozen forever.
    BUFFER_LOCKED
}



/*
    ABOUT PERIOD STATUSES: A period is never “re-opened”. Corrections happen in future periods via reversals.
    1. LOCKED (a.k.a. HARD CLOSED)
        Meaning
        The period is frozen forever.
        Characteristics

            ❌ No postings
            ❌ No reversals
            ❌ No adjustments
            ❌ No reopen

        ✔ Read-only
        ✔ Balances are final

        When does a period become LOCKED?
            After statutory reporting
            After tax filings
            After audit sign-off
            After N days post close (configurable)

    2. CLOSED (a.k.a. SOFT CLOSED)
        Meaning
        Operationally closed, but reversible.

        Characteristics
            ❌ Normal postings
            ✔ Reversal entries allowed
            ✔ Adjustments via reversal only
            ✔ Full audit trail
            ✔ Balances may change (via reversals)

        Why this exists
        Because reality is messy:
            Late invoices
            Corrections
            Regulatory adjustments
        But you never allow direct edits.

    3. OPEN: (Open for Posting)

    4. BUFFER CLOSED: (Buffer time before and after the closure windows for avoiding some conditions)
        Prevent:
            Half-posted journals
            Race conditions
            End-of-day chaos
            Concurrent close & post

        During buffer window:
            ❌ No postings
            ❌ No reversals
            ✔ Only validations
            ✔ Only scheduler jobs
        This is a hard freeze.
 */