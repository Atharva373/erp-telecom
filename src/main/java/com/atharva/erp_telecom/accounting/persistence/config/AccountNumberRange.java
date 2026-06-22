package com.atharva.erp_telecom.accounting.persistence.config;


import com.atharva.erp_telecom.accounting.enums.AccountCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_number_ranges")
public class AccountNumberRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Applies per category (Assets, Revenue, etc.) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private AccountCategory category;

    @Column(nullable = false)
    private Integer rangeStart;

    @Column(nullable = false)
    private Integer rangeEnd;

    /** Controls whether ERP assigns account numbers automatically */
    private boolean autoGenerate = false;

    /** Allows consultants to manually override account codes */
    private boolean allowManual = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    private LocalDate effectiveFrom = LocalDate.now();
    private LocalDate effectiveTo;

    // Add CompanyEntity entity field here if needed in the future
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedOn;

    private String createdBy;

    private String modifiedBy;


}
