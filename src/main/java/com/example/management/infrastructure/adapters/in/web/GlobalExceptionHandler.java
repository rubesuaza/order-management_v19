package com.example.management.infrastructure.adapters.in.web;

import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.exception.InvalidOrderItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderException(InvalidOrderException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "InvalidOrderException");
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(InvalidOrderItemException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderItemException(InvalidOrderItemException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "InvalidOrderItemException");
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "InternalServerError");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
