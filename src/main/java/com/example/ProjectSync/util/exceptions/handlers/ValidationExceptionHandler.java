package com.example.ProjectSync.util.exceptions.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.ProjectSync.util.exceptions.BadRequestException;

/**
 * ValidationExceptionHandler - handles validation-related exceptions.
 * 
 * Handles:
 * - BadRequestException (400 Bad Request)
 * - MethodArgumentNotValidException (400 Bad Request)
 * 
 * Returns HTTP 400 when:
 * - Request syntax is invalid
 * - Required fields are missing
 * - Data type validation fails
 */
public class ValidationExceptionHandler {

    /**
     * Handles BadRequestException (400 Bad Request)
     * Thrown when request contains syntactically invalid data.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors (400 Bad Request)
     * Thrown by Spring when @Valid annotation validation fails.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", "Validation failed");
        errorResponse.put("timestamp", LocalDateTime.now());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        errorResponse.put("errors", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * ErrorResponse - standard error response structure
     */
    public static class ErrorResponse {
        public int status;
        public String message;
        public LocalDateTime timestamp;

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }
    }
}
