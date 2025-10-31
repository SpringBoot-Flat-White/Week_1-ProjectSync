package com.example.ProjectSync.util.exceptions;

/**
 * ForbiddenException - thrown when a user does not have permission to access a resource.
 * Maps to HTTP 403 Forbidden response.
 * 
 * Note: This differs from 401 Unauthorized. The user is authenticated, but not authorized
 * to perform the requested action.
 * 
 * Example: User tries to delete a project they don't own
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
