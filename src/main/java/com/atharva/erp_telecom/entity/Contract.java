package com.atharva.erp_telecom.entity;


import com.atharva.erp_telecom.enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "contract")
@EntityListeners(AuditingEntityListener.class)
public class Contract {

    public Contract() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @Column(nullable = false, unique = true, updatable = false)
    private String contractNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDate contractStartDate;
    private LocalDate contractEndDate;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ContractStatus contractStatus = ContractStatus.INACTIVE;

    // NOTE: @JsonBackReference is used for mapping back to the Parent entity. This annotation is mostly used in the Child entity to prevent infinite recursion.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_agreement_id")
    @JsonBackReference
    private MasterAgreement masterAgreement;

    @Column(length = 255)
    private String deactivationReason;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime updatedOn;



    // Commented this as this needs to be handled at the Service layer instead of the Entity layer.

    //    private String generateContractNumber() {
    //        String customerId = String.valueOf(customer.getCustomerId());
    //        String productType = product.getProductCategory();
    //        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    //        return String.format("CON_%s_%s_%s",customerId,productType,currentTimestamp);
    //    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getDeactivationReason() {
        return deactivationReason;
    }

    public void setDeactivationReason(String deactivationReason) {
        this.deactivationReason = deactivationReason;
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

    public MasterAgreement getMasterAgreement() {
        return masterAgreement;
    }

    public void setMasterAgreement(MasterAgreement masterAgreement) {
        this.masterAgreement = masterAgreement;
    }
}