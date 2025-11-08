package com.example.ProjectSync.util.exceptions.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.ProjectSync.util.exceptions.DatabaseException;

/**
 * SystemExceptionHandler - handles system and database-level exceptions.
 * 
 * Handles:
 * - DatabaseException (500 Internal Server Error)
 * - General Exception (500 Internal Server Error)
 * 
 * Returns HTTP 500 when:
 * - Database operations fail
 * - Unexpected server errors occur
 * - System-level errors that cannot be recovered from
 * 
 * Note: 500 errors indicate server-side problems, not client errors.
 * These should be logged and monitored for debugging.
 */
public class SystemExceptionHandler {

    /**
     * Handles DatabaseException (500 Internal Server Error)
     * Thrown when database operations fail unexpectedly.
     */
    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Database operation failed: " + ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles general exceptions (500 Internal Server Error)
     * Fallback handler for any uncaught exceptions.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred: " + ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
