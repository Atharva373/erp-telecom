package com.atharva.erp_telecom.salesorder.persistence.transactional;

import com.atharva.erp_telecom.salesorder.enums.AgreementStatus;
import com.atharva.erp_telecom.salesorder.enums.AgreementType;
import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
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

@Setter
@Getter
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
    private BusinessEntity businessEntity;

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

}
