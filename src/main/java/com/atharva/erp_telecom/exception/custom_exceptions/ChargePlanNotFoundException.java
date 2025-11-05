package com.atharva.erp_telecom.exception.custom_exceptions;

public class ChargePlanNotFoundException extends RuntimeException{
    public ChargePlanNotFoundException(String message) {
        super(message);
    }
}
