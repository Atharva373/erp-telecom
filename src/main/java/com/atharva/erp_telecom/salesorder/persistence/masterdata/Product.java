package com.atharva.erp_telecom.salesorder.persistence.masterdata;


import com.atharva.erp_telecom.invoicing.persistence.masterdata.ChargePlan;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="product")
@EntityListeners(AuditingEntityListener.class)      // This is used as a listener class to tell JPA to listen to events like insert and update for
                                                    // this entity. The prePersist() , preUpdate() and similar methods are invoked before any event like update insert or delete.
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 50, unique = true)
    private String productCode;     // Uniques codes for each product.

    @Column(nullable = false,length = 100)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Column(nullable = false, length = 50)
    private String productCategory;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean bundle = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference       // This annotation is used to maintain parent-child hierarchy for inter-related entities.
    private Set<ChargePlan> chargePlans = new HashSet<>();

}
