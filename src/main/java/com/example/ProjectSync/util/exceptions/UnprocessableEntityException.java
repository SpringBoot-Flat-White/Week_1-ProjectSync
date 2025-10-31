package com.example.ProjectSync.util.exceptions;

/**
 * UnprocessableEntityException - thrown when a request is well-formed but contains semantic errors.
 * Maps to HTTP 422 Unprocessable Entity response.
 * 
 * Difference from 400 Bad Request:
 * - 400: The request syntax is invalid (e.g., missing required fields)
 * - 422: The request syntax is valid, but the semantic content violates business rules
 * 
 * Example: Project name is syntactically valid but contains restricted keywords
 */
public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
