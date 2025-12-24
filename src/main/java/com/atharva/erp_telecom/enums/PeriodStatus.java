package com.atharva.erp_telecom.enums;

public enum PeriodStatus {
    OPEN,   // Open --> The period is active and available for accounting entries.
    CLOSED, // Closed --> The period is finalized and no longer available for posting.
    LOCKED  // Locked --> The period is restricted, but not fully closed.
}
