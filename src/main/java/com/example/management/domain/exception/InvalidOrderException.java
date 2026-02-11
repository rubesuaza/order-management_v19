package com.example.management.domain.exception;

/**
 * Excepción de dominio que se lanza cuando se intenta realizar una operación inválida sobre una Order.
 */
public class InvalidOrderException extends RuntimeException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
}
