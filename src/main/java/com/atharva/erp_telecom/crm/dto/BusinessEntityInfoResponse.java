package com.atharva.erp_telecom.crm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessEntityInfoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String entityClass;
    private String entitySubClass;
    private String email;
    private Long contactNumber;
    private String region;
    private String governmentId;
}
