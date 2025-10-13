package com.atharva.erp_telecom.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "taxes")
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

    public Tax() {
    }

    public Long getId() {
        return taxId;
    }

    public void setId(Long taxId) {
        this.taxId = taxId;
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

    public Double getTotalTaxPercentage() {
        return cgstPercentage + sgstPercentage + igstPercentage;
    }
}