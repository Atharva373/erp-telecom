package com.atharva.erp_telecom.entity;

import com.atharva.erp_telecom.enums.EntryType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "posting_rule_lines")
@EntityListeners((AuditingEntityListener.class))
public class PostingRuleLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posting_rule_id")
    @JsonBackReference
    private PostingRule postingRule;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chart_of_account_id", nullable = false)
    private ChartOfAccount account;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false, length = 10)
    private EntryType entryType;    // DEBIT or CREDIT

    /**
     *  Optional: for control of line ordering in JEs.
     *  Best practice is to assign a sortOrder which will decide how Journal entries are sequenced based on the number assigned.<br><br>
     *  e.g.    | Order | EntryType | Account             | Amount Expression |<br><br>
     *          | ----- | --------- | ------------------- | ----------------- |<br><br>
     *          | 10    | DEBIT     | Accounts Receivable | GROSS_AMOUNT      |<br><br>
     *          | 20    | CREDIT    | Revenue             | NET_AMOUNT        |<br><br>
     *          | 30    | CREDIT    | GST Payable         | TAX_AMOUNT        |<br><br>
     * <br><br>
     *  SAMPLE VALUES:<br><br>
     *  10 Debit A/R<br><br>
     *  15 Debit A/R - Interest<br><br>
     *  20 Credit Revenue<br><br>
     *  21 Credit Discount Revenue<br><br>
     *  30 Credit Tax CGST<br><br>
     *  31 Credit Tax SGST<br><br>
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * Optional: line-level condition -->  When to Post ?
     * Example: "TAX_ZONE == 'INTRA'" or "CUSTOMER_TYPE == 'POSTPAID'"
     */
    @Column(name = "item_condition_expression", length = 1000)
    private String itemConditionExpression;

    // Formula or multiplier if dynamic --> How much to post ?
    /**
     * Expression for amount calculation from the posting context.
     * Example: "GROSS_AMOUNT", "NET_AMOUNT", or "NET_AMOUNT - DISCOUNT"
     */
    private String amountExpression; // e.g., "event.amount * 1.0" or "event.taxAmount"

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

    @Column
    private String createdBy;

    @Column
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getItemConditionExpression() {
        return itemConditionExpression;
    }

    public void setItemConditionExpression(String itemConditionExpression) {
        this.itemConditionExpression = itemConditionExpression;
    }
}
