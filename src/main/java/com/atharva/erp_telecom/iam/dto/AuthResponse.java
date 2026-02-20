package com.atharva.erp_telecom.iam.dto;

public class AuthResponse {
    private String jwt;

    public AuthResponse(String jwt){
        this.jwt = jwt;
    }

    public String getJwt(){
        return jwt;
    }
}
