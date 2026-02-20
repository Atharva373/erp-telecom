package com.atharva.erp_telecom.accounting.persistence.masterdata;

import com.atharva.erp_telecom.crm.enums.PartyType;
import com.atharva.erp_telecom.accounting.enums.SubledgerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "subledger_account",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subledger_account_party",
                        columnNames = {
                                "company_code",
                                "subledger_type",
                                "party_type",
                                "party_id"
                        }
                )
        }
)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class SubledgerAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "subledger_type", nullable = false)
    private SubledgerType subledgerType; // AR / AP

    @Enumerated(EnumType.STRING)
    @Column(name = "party_type", nullable = false)
    private PartyType partyType; // CUSTOMER / VENDOR

    @Column(name = "party_id", nullable = false)
    private Long partyId; // CustomerId or VendorId (later BusinessEntityId)

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn = LocalDateTime.now();

}

