package com.example.management.domain.exception;

/**
 * Excepción de dominio que se lanza cuando se intenta crear un OrderItem inválido.
 */
public class InvalidOrderItemException extends RuntimeException {
    
    public InvalidOrderItemException(String message) {
        super(message);
    }
}
