package com.example.ProjectSync.util.exceptions.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.ProjectSync.util.exceptions.ForbiddenException;

/**
 * AuthenticationExceptionHandler - handles authentication and authorization exceptions.
 * 
 * Handles:
 * - ForbiddenException (403 Forbidden)
 * 
 * Returns HTTP 403 when:
 * - User is authenticated but lacks permission
 * - User attempts to access restricted resources
 * - User attempts to perform unauthorized actions
 * 
 * Note: This handler focuses on 403 Forbidden (authorization).
 * 401 Unauthorized (authentication) would be added here if implementing authentication.
 */
public class AuthenticationExceptionHandler {

    /**
     * Handles ForbiddenException (403 Forbidden)
     * Thrown when the user is authenticated but not authorized to access the resource.
     * 
     * Teaching Point:
     * 403 Forbidden = "I know who you are, but you can't do that"
     * 401 Unauthorized = "I don't know who you are"
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
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
