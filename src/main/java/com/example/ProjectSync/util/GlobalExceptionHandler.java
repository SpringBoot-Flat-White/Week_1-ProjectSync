package com.example.ProjectSync.util;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.example.ProjectSync.util.exceptions.handlers.AuthenticationExceptionHandler;
import com.example.ProjectSync.util.exceptions.handlers.BusinessLogicExceptionHandler;
import com.example.ProjectSync.util.exceptions.handlers.ResourceExceptionHandler;
import com.example.ProjectSync.util.exceptions.handlers.SystemExceptionHandler;
import com.example.ProjectSync.util.exceptions.handlers.ValidationExceptionHandler;

/**
 * GlobalExceptionHandler - Centralized orchestrator for exception handling.
 * 
 * This class acts as the main entry point for Spring's exception handling,
 * delegating specific exception types to specialized handler classes organized
 * by concern/category.
 * 
 * Architecture:
 * - GlobalExceptionHandler (@ControllerAdvice) - Orchestrator
 *   ├── ValidationExceptionHandler (400 Bad Request)
 *   ├── AuthenticationExceptionHandler (403 Forbidden)
 *   ├── ResourceExceptionHandler (404 Not Found)
 *   ├── BusinessLogicExceptionHandler (409 Conflict, 422 Unprocessable Entity)
 *   └── SystemExceptionHandler (500 Internal Server Error)
 * 
 * Benefits:
 * - ✅ Clear separation of concerns by exception category
 * - ✅ Each handler focused on specific HTTP status codes
 * - ✅ Easier to maintain and extend
 * - ✅ Better organized than a single monolithic handler class
 * - ✅ Follows Single Responsibility Principle
 * 
 * Handler Classes:
 * 1. ValidationExceptionHandler
 *    - BadRequestException (400)
 *    - MethodArgumentNotValidException (400)
 * 
 * 2. AuthenticationExceptionHandler
 *    - ForbiddenException (403)
 * 
 * 3. ResourceExceptionHandler
 *    - ResourceNotFoundException (404)
 * 
 * 4. BusinessLogicExceptionHandler
 *    - ConflictException (409)
 *    - UnprocessableEntityException (422)
 * 
 * 5. SystemExceptionHandler
 *    - DatabaseException (500)
 *    - General Exception (500)
 */
@ControllerAdvice(basePackageClasses = {
    ValidationExceptionHandler.class,
    AuthenticationExceptionHandler.class,
    ResourceExceptionHandler.class,
    BusinessLogicExceptionHandler.class,
    SystemExceptionHandler.class
})
public class GlobalExceptionHandler {
    
    // This class acts as an orchestrator/dispatcher.
    // All exception handling has been delegated to specialized handler classes:
    // - ValidationExceptionHandler.java → Handles 400 errors
    // - AuthenticationExceptionHandler.java → Handles 403 errors
    // - ResourceExceptionHandler.java → Handles 404 errors
    // - BusinessLogicExceptionHandler.java → Handles 409 and 422 errors
    // - SystemExceptionHandler.java → Handles 500 errors
    // 
    // The @ControllerAdvice annotation registers this class along with all
    // handler classes, allowing Spring to route exceptions to the appropriate handler.
}
