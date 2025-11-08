package com.example.ProjectSync.util.exceptions;

/**
 * ConflictException - thrown when a request conflicts with the current state of the resource.
 * Maps to HTTP 409 Conflict response.
 * 
 * Example: Attempting to move a COMPLETED project back to PENDING status
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
