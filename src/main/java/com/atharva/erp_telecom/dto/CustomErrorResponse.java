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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
