package com.atharva.erp_telecom.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "taxes")
@EntityListeners(AuditingEntityListener.class)
@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','AGENT','MANAGER')")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taxId;

    @Column(nullable = false, unique = true)
    private String taxCode; // e.g., "GST_STANDARD"

    @Column(nullable = false)
    private String taxName; // e.g., "Goods and Services Tax"

    @Column(nullable = false)
    private Double cgstPercentage;

    @Column(nullable = false)
    private Double sgstPercentage;

    @Column(nullable = false)
    private Double igstPercentage;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 1000)
    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @Column
    private LocalDate effectiveFrom;

    @Column
    private LocalDate effectiveTo;

    public Tax() {
    }


    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Double getCgstPercentage() {
        return cgstPercentage;
    }

    public void setCgstPercentage(Double cgstPercentage) {
        this.cgstPercentage = cgstPercentage;
    }

    public Double getSgstPercentage() {
        return sgstPercentage;
    }

    public void setSgstPercentage(Double sgstPercentage) {
        this.sgstPercentage = sgstPercentage;
    }

    public Double getIgstPercentage() {
        return igstPercentage;
    }

    public void setIgstPercentage(Double igstPercentage) {
        this.igstPercentage = igstPercentage;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
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

    public Double getTotalTaxPercentage() {
        return cgstPercentage + sgstPercentage + igstPercentage;
    }
}