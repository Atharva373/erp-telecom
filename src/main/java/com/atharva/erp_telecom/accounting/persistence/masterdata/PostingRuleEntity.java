package com.atharva.erp_telecom.accounting.persistence.masterdata;

import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.accounting.enums.AccountingEventType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// NOTE: For this model, Posting rules are global to all the CompanyEntity Codes

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
@Setter
@Getter
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

public class PostingRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Natural key / code for reference in configs
    @Column(nullable = false, length = 100)
    private String postingRuleCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountingEventType eventType;

    // Rule belongs to specific companyEntity OR global (i.e. companyEntity = null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity companyEntity;

    @OneToMany(
            mappedBy = "postingRule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<PostingRuleLineEntity> lines = new ArrayList<>();

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
     * More granular line-level conditions sit in PostingRuleLineEntity.
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
}

