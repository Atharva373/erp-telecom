package com.atharva.erp_telecom.exception.custom_exceptions;

// Thrown whenever the user has insufficient privileges for the API called.
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
