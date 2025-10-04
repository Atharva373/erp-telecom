package com.atharva.erp_telecom.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class CustomErrorResponse {
    private LocalDateTime timestamp;
    private HttpStatus statusCode;
    private String error;
    private String message;
    private String path;

    public CustomErrorResponse(LocalDateTime timestamp, HttpStatus statusCode, String error, String path, String message) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.error = error;
        this.path = path;
        this.message = message;
    }
}
