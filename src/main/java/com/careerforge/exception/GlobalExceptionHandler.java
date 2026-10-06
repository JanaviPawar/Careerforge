// src/main/java/com/careerforge/exception/GlobalExceptionHandler.java
package com.careerforge.exception;

import com.careerforge.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
/*
 * @RestControllerAdvice intercepts every exception thrown from
 * any @RestController and handles it here — centrally.
 *
 * OOP POLYMORPHISM: Spring picks the most specific @ExceptionHandler.
 * ResourceNotFoundException → first handler (most specific).
 * Any other exception → last handler (least specific / fallback).
 *
 * LAYERED ARCHITECTURE BENEFIT: Controllers stay clean.
 * They just call services; if anything throws, this class catches it.
 */
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)   // 404
    public ApiResponse<Void> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)   // 401
    public ApiResponse<Void> handleBadCredentials(BadCredentialsException ex) {
        return ApiResponse.error("Invalid email or password");
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleUnauthorized(UnauthorizedException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)   // 403
    public ApiResponse<Void> handleAccessDenied(AccessDeniedException ex) {
        return ApiResponse.error("You do not have permission to perform this action");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)   // 400
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleFileUpload(FileUploadException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // Catches @Valid failures — returns which fields failed and why
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field   = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            errors.put(field, message);
        });
        return ApiResponse.<Map<String, String>>builder()
                .success(false).message("Validation failed")
                .data(errors).build();
    }

    // FALLBACK — catches anything not caught above
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // 500
    public ApiResponse<Void> handleGeneric(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ApiResponse.error("An unexpected error occurred. Please try again.");
    }
}
