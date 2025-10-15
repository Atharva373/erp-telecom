package com.atharva.erp_telecom.dto;

public class TaxResponse {
    private Long taxId;
    private String taxCode;
    private String taxName;
    private Double cgstPercentage;
    private Double sgstPercentage;
    private Double igstPercentage;
    private Double totalTaxPercentage;
    private Boolean isActive;
    private String description;

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
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

    public Double getTotalTaxPercentage() {
        return totalTaxPercentage;
    }

    public void setTotalTaxPercentage(Double totalTaxPercentage) {
        this.totalTaxPercentage = totalTaxPercentage;
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
}
