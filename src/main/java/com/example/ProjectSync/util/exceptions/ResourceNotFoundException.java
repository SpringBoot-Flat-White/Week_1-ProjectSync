package com.example.ProjectSync.util.exceptions;

/**
 * ResourceNotFoundException - thrown when a requested resource is not found.
 * Maps to HTTP 404 Not Found response.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
