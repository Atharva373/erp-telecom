package com.atharva.erp_telecom.entity.charging;


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

// Used for implementing Usage based scenarios for Prepaid services. Once a UDR hits this entity it consumes some values from the bucket.

// Transient getters are created for user discretion and information on how much data is used and left in the bucket.

@Entity
@Table(name = "usage_bucket")
public class UsageBucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usageBucketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_plan_id", nullable = false)
    private ChargePlan chargePlan;

    @Column(nullable = false, length = 20)
    private String usageType;       // DATA, VOICE, SMS , FTTH

    @Column(nullable = false)
    private Long allocatedUnits;  // e.g. 1GB/day -> stored as 1073741824 bytes

    @Column(nullable = false)
    private Long consumedUnits = 0L;  // updated via UDRs

    @Column(nullable = false)
    private LocalDateTime bucketStart;

    @Column(nullable = false)
    private LocalDateTime bucketEnd;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedOn;

    // Getters and setters.
    public Long getUsageBucketId() {
        return usageBucketId;
    }

    public void setUsageBucketId(Long usageBucketId) {
        this.usageBucketId = usageBucketId;
    }

    public ChargePlan getChargePlan() {
        return chargePlan;
    }

    public void setChargePlan(ChargePlan chargePlan) {
        this.chargePlan = chargePlan;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Long getAllocatedUnits() {
        return allocatedUnits;
    }

    public void setAllocatedUnits(Long allocatedUnits) {
        this.allocatedUnits = allocatedUnits;
    }

    public Long getConsumedUnits() {
        return consumedUnits;
    }

    public void setConsumedUnits(Long consumedUnits) {
        this.consumedUnits = consumedUnits;
    }

    public LocalDateTime getBucketStart() {
        return bucketStart;
    }

    public void setBucketStart(LocalDateTime bucketStart) {
        this.bucketStart = bucketStart;
    }

    public LocalDateTime getBucketEnd() {
        return bucketEnd;
    }

    public void setBucketEnd(LocalDateTime bucketEnd) {
        this.bucketEnd = bucketEnd;
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

    // Helpers : Transient fields i.e. these fields won't persist in the database (no entry in DB) and will only be in memory.
    // Jackson uses getters to serialize JSONs, thus these fields are only in memory.
    // Always use @Transient to derive and return calculative fields in the response.

    @Transient
    public Long getRemainingUnits() {
        return allocatedUnits - consumedUnits;
    }

    @Transient
    public double getUsagePercentage() {
        if (allocatedUnits == null || allocatedUnits == 0) {
            return 0.0;
        }
        return ((double) consumedUnits / allocatedUnits) * 100.0;
    }

    @Transient
    public String getUsageStatus() {
        double percentage = getUsagePercentage();
        if (percentage >= 90) {
            return "CRITICAL";
        } else if (percentage >= 75) {
            return "WARNING";
        } else {
            return "NORMAL";
        }
    }
}
