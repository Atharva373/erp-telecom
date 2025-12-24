package com.atharva.erp_telecom.entity.accounting;

import com.atharva.erp_telecom.entity.salesorder.Company;
import com.atharva.erp_telecom.enums.AccountingEventType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// NOTE: For this model, Posting rules are global to all the Company Codes

// Header entity for a posting rule

// NOTES: Regarding @UniqueConstraint in JPA and Database
/*
    #   What are 'Unique Constraints' ?
    --> It tells the JPA provider (Hibernate in Spring Boot) to generate a UNIQUE constraint at the database level when DDL is created.
        In short, it creates a check at the DB level to see if in a particular row, the combination of all the columnNames is unique.
        Here, if combination of "posting_rule_code", "company_id", "effective_from" is not unique, the DB will throw an error which will be
        raised as an exception inside JPA (DataIntegrityViolationException).
        At DB level, the annotation translates to:

        CREATE TABLE posting_rules (
           ....
            CONSTRAINT uk_posting_rule_by_company UNIQUE ("posting_rule_code", "company_id", "effective_from"),
            PRIMARY KEY (id)
        );

 */
@Entity
@Table(
        name = "posting_rules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_posting_rule_by_company",
                        columnNames = {"posting_rule_code", "company_id", "effective_from"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class PostingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Natural key / code for reference in configs
    @Column(nullable = false, length = 100)
    private String postingRuleCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountingEventType eventType;

    // Rule belongs to specific company OR global (i.e. company = null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(
            mappedBy = "postingRule",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<PostingRuleLine> items = new ArrayList<>();

    @Column(length = 1000)
    private String description;

    @Column
    private boolean active = true;

    @Column
    private LocalDate effectiveFrom = LocalDate.now();

    @Column
    private LocalDate effectiveTo;

    // Optional: dynamic condition (e.g., PREPAID vs POSTPAID, payment method, charge type)
    // This indicates 'When should the rule apply?' or simply 'When to post?'
    /**
     * Optional rule-level condition.
     * e.g. "CUSTOMER_TYPE == 'POSTPAID'"
     * More granular line-level conditions sit in PostingRuleLine.
     */
    @Column(length = 1000, name = "header_condition_expression")
    private String headerConditionExpression; // future evaluator logic

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

    public String getPostingRuleCode() {
        return postingRuleCode;
    }

    public void setPostingRuleCode(String postingRuleCode) {
        this.postingRuleCode = postingRuleCode;
    }

    public AccountingEventType getEventType() {
        return eventType;
    }

    public void setEventType(AccountingEventType eventType) {
        this.eventType = eventType;
    }

    public List<PostingRuleLine> getItems() {
        return items;
    }

    public void setItems(List<PostingRuleLine> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getHeaderConditionExpression() {
        return headerConditionExpression;
    }

    public void setHeaderConditionExpression(String headerConditionExpression) {
        this.headerConditionExpression = headerConditionExpression;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

