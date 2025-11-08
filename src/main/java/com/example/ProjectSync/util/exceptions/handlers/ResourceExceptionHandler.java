package com.example.ProjectSync.util.exceptions.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.ProjectSync.util.exceptions.ResourceNotFoundException;

/**
 * ResourceExceptionHandler - handles resource-related exceptions.
 * 
 * Handles:
 * - ResourceNotFoundException (404 Not Found)
 * 
 * Returns HTTP 404 when:
 * - Requested resource doesn't exist
 * - Resource ID is invalid or not found in database
 * - User doesn't have visibility to the resource
 */
public class ResourceExceptionHandler {

    /**
     * Handles ResourceNotFoundException (404 Not Found)
     * Thrown when a requested resource cannot be found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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
