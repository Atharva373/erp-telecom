package com.atharva.erp_telecom.accounting.persistence.masterdata;

import com.atharva.erp_telecom.accounting.enums.EntryType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "posting_rule_lines")
@EntityListeners(AuditingEntityListener.class)
public class PostingRuleLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posting_rule_id")
    @JsonBackReference
    private PostingRuleEntity postingRuleEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id",nullable = false)
    private ChartOfAccountEntity account;

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
     *  30 Credit TaxEntity CGST<br><br>
     *  31 Credit TaxEntity SGST<br><br>
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

}
