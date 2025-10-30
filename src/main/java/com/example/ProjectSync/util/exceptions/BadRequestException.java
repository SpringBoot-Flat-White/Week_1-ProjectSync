package com.example.ProjectSync.util.exceptions;

/**
 * BadRequestException - thrown when a request contains invalid data.
 * Maps to HTTP 400 Bad Request response.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
