package com.atharva.erp_telecom.finance.persistence.masterdata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "currencies")
@EntityListeners(AuditingEntityListener.class)
public class CurrencyEntity {

    @Id
    @Column(length = 3)
    private String currencyCode; // ISO-4217: INR, USD, EUR

    @Column(nullable = false, length = 50)
    private String currencyName; // Indian Rupee, US Dollar

    @Column(nullable = false)
    private Integer decimalPlaces; // 2 for INR, 0 for JPY

    @Column(length = 5)
    private String symbol; // ₹, $, €

    @Column(nullable = false)
    private boolean active = true;

    // Optional audit
    @CreatedDate
    private LocalDateTime createdOn;

    @LastModifiedDate
    private LocalDateTime modifiedOn;

}
