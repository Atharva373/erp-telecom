package com.atharva.erp_telecom.entity;

import com.atharva.erp_telecom.enums.EntryType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "posting_rule_lines")
public class PostingRuleLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posting_rule_id")
    @JsonBackReference
    private PostingRule postingRule;

    @ManyToOne(optional = false)
    private ChartOfAccount account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType; // DEBIT or CREDIT

    // Formula or multiplier if dynamic
    // How much to post ?
    private String amountExpression; // e.g., "event.amount * 1.0" or "event.taxAmount"

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

    public PostingRule getPostingRule() {
        return postingRule;
    }

    public void setPostingRule(PostingRule postingRule) {
        this.postingRule = postingRule;
    }

    public ChartOfAccount getAccount() {
        return account;
    }

    public void setAccount(ChartOfAccount account) {
        this.account = account;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public String getAmountExpression() {
        return amountExpression;
    }

    public void setAmountExpression(String amountExpression) {
        this.amountExpression = amountExpression;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
