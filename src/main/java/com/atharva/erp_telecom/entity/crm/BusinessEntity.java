package com.atharva.erp_telecom.entity.crm;


import com.atharva.erp_telecom.enums.BusinessEntityType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "business_entity")
@EntityListeners(AuditingEntityListener.class)
@Data
public class BusinessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long businessEntityId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessEntityType entityType; // INDIVIDUAL / ENTERPRISE

    private String firstName;
    private String lastName;

    /**
     * Legal name for enterprises
     * Example: "Photon Networks LLP"
     */
    private String legalName;

    /**
     * Classification hooks (CRM / Reporting / Pricing)
     */
    private String entityClass;
    private String entitySubClass;

    /* ---------- Contact ---------- */

    private String email;
    private Long contactNumber;

    /* ---------- Legal / Compliance ---------- */

    /**
     * PAN / GSTIN / Aadhaar / Passport etc
     */
    private String governmentId;

    private String country;
    private String countryCode;

    /* ---------- Address ---------- */

    private String addressLine1;
    private String addressLine2;
    private String region;

    /* ---------- Audit ---------- */

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedOn;

    /* ---------- Derived ---------- */

    public String getDisplayName() {
        if (entityType == BusinessEntityType.ENTERPRISE) {
            return legalName;
        }
        return lastName + ", " + firstName;
    }
}