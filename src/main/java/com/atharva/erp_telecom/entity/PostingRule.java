package com.atharva.erp_telecom.entity;

import com.atharva.erp_telecom.enums.AccountingEventType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Header entity for a posting rule
@Entity
@Table(name = "posting_rules")
public class PostingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountingEventType eventType;

    @OneToMany(
            mappedBy = "postingRule",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<PostingRuleLine> items;

    private String description;

    private boolean active = true;

    private LocalDate effectiveFrom = LocalDate.now();
    private LocalDate effectiveTo;

    // Optional: dynamic condition (e.g., PREPAID vs POSTPAID, payment method, charge type)
    // This indicates 'When should the rule apply?' or simply 'When to post?'
    private String conditionExpression; // future evaluator logic

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

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
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

