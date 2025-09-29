package com.atharva.erp_telecom.entity;


import com.atharva.erp_telecom.enums.BillingCycle;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "charge_plan")
@EntityListeners(AuditingEntityListener.class)
public class ChargePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chargePlanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(nullable = false,length = 30)
    private String planType;        // PREPAID or POSTPAID

    @Column(precision = 10, scale = 2)
    private BigDecimal recurringCharge;
    private Double oneOffCharge;
    private Double ratePerUnit;     // Applicable if in case of USAGE based scenarios.
                                    // This is the base price for a postpaid plan even when there is no usage by the user

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;    // Describes when the items are billed and invoiced.

    private Integer validityDays;    // Describes the validity of the product in days

    @Column(nullable = false, length = 10)
    private String currency;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    public Long getChargePlanId() {
        return chargePlanId;
    }

    public void setChargePlanId(Long chargePlanId) {
        this.chargePlanId = chargePlanId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public BigDecimal getRecurringCharge() {
        return recurringCharge;
    }

    public void setRecurringCharge(BigDecimal recurringCharge) {
        this.recurringCharge = recurringCharge;
    }

    public Double getOneOffCharge() {
        return oneOffCharge;
    }

    public void setOneOffCharge(Double oneOffCharge) {
        this.oneOffCharge = oneOffCharge;
    }

    public Double getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Double ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
}
