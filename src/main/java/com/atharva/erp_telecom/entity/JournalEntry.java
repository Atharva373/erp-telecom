package com.atharva.erp_telecom.entity;

import com.atharva.erp_telecom.enums.AccountingEventType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journal_entries")
@EntityListeners(AuditingEntityListener.class)
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountingEventType eventType;

    @Column(nullable = false)
    private Long companyId;

    /** Reference to rule used */
    @Column(nullable = false)
    private Long postingRuleId;

    @Column(nullable = false)
    private String ledger = "PRIMARY";

    /** External event reference (Invoice ID, Payment ID, etc.) */
    private String sourceTransactionId;

    /** Allows traceability for batch runs, integrations */
    private String sourceSystem;

    // DR & CR totals for quick financial validation
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDebit;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCredit;

    @Column(nullable = false)
    private LocalDateTime postingDate;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalEntryLine> lines = new ArrayList<>();

    @Column(nullable = false,length = 3)
    private String currencyCode;

    // Auto-calculated, validated: SUM of all DEBIT lines == SUM of all CREDIT lines
    private boolean balanced;

    private boolean posted = true;   // In some ERPs, JE is draft until posted
    private boolean reversed = false;

    @Column(name = "reversal_of")
    private Long reversalOf; // ID of JE it reverses

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

    private String createdBy;
    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountingEventType getEventType() {
        return eventType;
    }

    public void setEventType(AccountingEventType eventType) {
        this.eventType = eventType;
    }

    public Long getPostingRuleId() {
        return postingRuleId;
    }

    public void setPostingRuleId(Long postingRuleId) {
        this.postingRuleId = postingRuleId;
    }

    public String getSourceTransactionId() {
        return sourceTransactionId;
    }

    public void setSourceTransactionId(String sourceTransactionId) {
        this.sourceTransactionId = sourceTransactionId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public LocalDateTime getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDateTime postingDate) {
        this.postingDate = postingDate;
    }

    public List<JournalEntryLine> getLines() {
        return lines;
    }

    public void setLines(List<JournalEntryLine> lines) {
        this.lines = lines;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public Long getReversalOf() {
        return reversalOf;
    }

    public void setReversalOf(Long reversalOf) {
        this.reversalOf = reversalOf;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
