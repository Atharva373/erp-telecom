package com.atharva.erp_telecom.exception;


import com.atharva.erp_telecom.dto.crm.CustomErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.atharva.erp_telecom.exception.custom_exceptions.*;
import org.springframework.web.context.request.WebRequest;

// The primary objective of this class will be to handle all the custom exceptions.
// This @RestControllerAdvice is only triggered when exceptions are propagated to and from the controller.
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(HttpStatus status,String message){
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",status.value());
        body.put("error",status.getReasonPhrase());
        body.put("message",message);
        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException e){
        return buildResponse(HttpStatus.CONFLICT,e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFound(RoleNotFoundException e){
        return buildResponse(HttpStatus.BAD_REQUEST,e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e){
        return buildResponse(HttpStatus.NOT_FOUND,e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<Object> handleInvalidJwt(InvalidJwtAuthenticationException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtTokenException.class)
    public ResponseEntity<Object> handleMalformedJwt(MalformedJwtTokenException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Access Denied. Insufficient Privilege.");
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Custom ExceptionHandling for Product GET API
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleProductNotFound(ProductNotFoundException exception, HttpServletRequest request){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Requested Product NOT FOUND",
                request.getRequestURI(),
                exception.getMessage()

        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
    /**
     * Custom Error handling for ChargePlan not found.
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleChargePlanNotFound(ChargePlanNotFoundException exception, HttpServletRequest request){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Requested ChargePlan NOT FOUND",
                request.getRequestURI(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
    /**
     * Custom Error handling for Illegal Posting rule.
     */
    @ExceptionHandler(IllegalPostingRuleException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalPostingRule(IllegalPostingRuleException exception, HttpServletRequest request){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Illegal operation in the Posting Rules.",
                request.getRequestURI(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    /**
     * Custom ExceptionHandling for Order GET API
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleOrderNotFound(OrderNotFoundException exception, HttpServletRequest request){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Requested Order NOT FOUND",
                request.getRequestURI(),
                exception.getMessage()

        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PostingPeriodException.class)
    public ResponseEntity<CustomErrorResponse> handlePostingPeriodException(PostingPeriodException exception, HttpServletRequest request){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Illegal Operation in Specified Period.",
                request.getRequestURI(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    // Generic method for handling anything else that the sever is unable to handle
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage() + Arrays.toString(ex.getStackTrace()));
    }
}
