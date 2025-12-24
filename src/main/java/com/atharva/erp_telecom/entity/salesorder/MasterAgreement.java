package com.atharva.erp_telecom.entity.salesorder;

import com.atharva.erp_telecom.entity.crm.Customer;
import com.atharva.erp_telecom.enums.AgreementStatus;
import com.atharva.erp_telecom.enums.AgreementType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "master_agreement")
@EntityListeners(AuditingEntityListener.class)
public class MasterAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agreementId;

    @Column(unique = true, nullable = false)
    private String agreementNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // NOTE: @JsonManagedReference is used for mapping the Child entity. This annotation is mostly used in the Parent entity to prevent infinite recursion.
    @OneToMany(mappedBy = "masterAgreement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference   // Forward mapping to the child entity (since Contract : MasterAgreement = N:1)
    private List<Contract> contracts = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(length = 1000)
    private String termsAndConditions;

    @Enumerated(EnumType.STRING)
    @Column
    private AgreementStatus status;

    @Enumerated(EnumType.STRING)
    @Column
    private AgreementType agreementType;

    @Column(length = 500)
    private String remarks;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @Column(length = 100)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;
    
    @PrePersist
    public void onCreate() {
        if (this.status == null) {
            this.status = AgreementStatus.PENDING;
        }
        if(this.agreementType == null){
            this.agreementType = AgreementType.STANDARD;
        }
    }

//  Commenting this code as this will be handled at the Service layer.
//    private String generateAgreementNumber() {
//        String customerPart = String.valueOf(customer.getCustomerId());
//        String timestampPart = LocalDateTime.now()
//                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        String productType = "";
//        if (!contracts.isEmpty()) {
//            Product firstProduct = contracts.get(0).getProduct();
//            if (firstProduct != null && firstProduct.getChargePlans() != null && !firstProduct.getChargePlans().isEmpty()) {
//                productType = firstProduct.getChargePlans().iterator().next().getPlanType().toString();
//            }
//        }
//        return String.format("AGR_%s_%s_%s", customerPart, productType ,timestampPart);
//    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public void setStatus(AgreementStatus status) {
        this.status = status;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public AgreementType getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(AgreementType agreementType) {
        this.agreementType = agreementType;
    }
}
