package com.example.ProjectSync.util.exceptions.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.ProjectSync.util.exceptions.ConflictException;
import com.example.ProjectSync.util.exceptions.UnprocessableEntityException;

/**
 * BusinessLogicExceptionHandler - handles business logic and semantic validation exceptions.
 * 
 * Handles:
 * - ConflictException (409 Conflict)
 * - UnprocessableEntityException (422 Unprocessable Entity)
 * 
 * Returns HTTP 409 when:
 * - Request conflicts with current resource state
 * - State machine transitions are violated
 * - Business rules prevent the operation
 * 
 * Returns HTTP 422 when:
 * - Request syntax is valid but contains semantic errors
 * - Business validation fails
 * - Data violates business rules
 */
public class BusinessLogicExceptionHandler {

    /**
     * Handles ConflictException (409 Conflict)
     * Thrown when a request conflicts with the current state of the resource.
     * 
     * Example: Trying to revert a COMPLETED project to PENDING status
     * violates the project state machine.
     * 
     * Teaching Point:
     * 409 is for state conflicts, not syntax errors.
     * The request is well-formed, but the business logic rejects it.
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(409));
    }

    /**
     * Handles UnprocessableEntityException (422 Unprocessable Entity)
     * Thrown when the request is well-formed but contains semantic errors.
     * 
     * Example: Project name is syntactically valid but contains restricted keywords.
     * 
     * Difference from 400 Bad Request:
     * - 400: Syntax is invalid (e.g., missing required field)
     * - 422: Syntax is valid, but semantic content violates business rules
     * 
     * Teaching Point:
     * 422 is stricter than 400. Use it when the JSON parses perfectly,
     * but the business logic rejects the values.
     */
    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleUnprocessableEntity(UnprocessableEntityException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
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
