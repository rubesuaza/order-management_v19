package com.example.management.infrastructure.adapters.out.persistence.entity;

/**
 * Enum que representa los estados de una orden en la base de datos.
 */
public enum OrderStatusEntity {
    PENDING,
    CONFIRMED,
    SHIPPED,
    CANCELLED
}
