package com.example.ProjectSync.util.exceptions;

/**
 * DatabaseException - thrown when a database operation fails.
 * Maps to HTTP 500 Internal Server Error response.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
