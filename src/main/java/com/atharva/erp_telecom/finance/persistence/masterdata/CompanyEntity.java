package com.atharva.erp_telecom.finance.persistence.masterdata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
@ToString
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false, unique = true, length = 50)
    private String companyCode; // e.g. "PHOTON_MH", "PHOTON_GJ"

    @Column(nullable = false, length = 150)
    private String companyName; // e.g. "Photon Networks LLP - Maharashtra"

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 10)
    private String stateCode; // GST state code (e.g. "27" for Maharashtra)

    @Column(length = 10)
    private String gstCode;

    @Column(length = 10)
    private String pinCode;

    @Column(length = 15)
    private String gstNumber;

    @Column(length = 50)
    private String panNumber;

    @Column(length = 50)
    private String cinNumber; // CompanyEntity Identification Number (optional)

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private Boolean parent = false;

    // --- Parent / Child Relationship ---
    /*
        How does JPA track recursive self-relations ?
        JPA scans this entity, due to the annotation @Entity. It sees that it has a Primary key 'companyId'.
        Now when another nested field has the type CompanyEntity and has an annotation @JoinColumn,
        it tells JPA that --> the value in this field (parent_company_id) for instance is a foreign key reference to the
        original CompanyEntity entity.
        So this column(parent_company_id) will store a reference to another CompanyEntity which will have (company_id) = 1.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_company_id")
    @JsonManagedReference
    private CompanyEntity parentCompany;

    @OneToMany(mappedBy = "parentCompany", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<CompanyEntity> childCompanies;

    // --- Audit Fields ---
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @Column(length = 100, updatable = false)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;
}
