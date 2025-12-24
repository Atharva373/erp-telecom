package com.atharva.erp_telecom.dto.charging;

import java.math.BigDecimal;

public class ChargePlanResponse {
    private Long chargePlanId;
    private String planType;     // PREPAID or POSTPAID
    private BigDecimal recurringCharge;
    private BigDecimal oneOffCharge;
    private Double ratePerUnit;
    private Integer validityDays;
    private String currency;

    public Long getChargePlanId() {
        return chargePlanId;
    }

    public void setChargePlanId(Long chargePlanId) {
        this.chargePlanId = chargePlanId;
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

    public BigDecimal getOneOffCharge() {
        return oneOffCharge;
    }

    public void setOneOffCharge(BigDecimal oneOffCharge) {
        this.oneOffCharge = oneOffCharge;
    }

    public Double getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Double ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
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
}
