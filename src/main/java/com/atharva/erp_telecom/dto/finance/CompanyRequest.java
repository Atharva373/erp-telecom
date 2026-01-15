package com.atharva.erp_telecom.dto.finance;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyRequest {
    private String companyCode;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String stateCode;
    private String gstCode;
    private String gstNumber;
    private String panNumber;
    private String cinNumber;
    private String email;
    private String phoneNumber;
    private String currencyCode;
    private Boolean isParent;
    private Long parentCompanyId;

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }
}
