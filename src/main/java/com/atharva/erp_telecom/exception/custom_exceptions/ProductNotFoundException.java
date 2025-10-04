package com.atharva.erp_telecom.exception.custom_exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id) {
        super("Product with ID: "+id+" does not exist.");
    }
}
