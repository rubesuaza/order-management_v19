package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderItemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem Tests")
class OrderItemTest {

    @Test
    @DisplayName("Debería crear un OrderItem válido")
    void shouldCreateValidOrderItem() {
        // Given
        String productId = "PROD-001";
        Integer quantity = 2;
        BigDecimal price = new BigDecimal("29.99");

        // When
        OrderItem item = new OrderItem(productId, quantity, price);

        // Then
        assertNotNull(item);
        assertEquals(productId, item.getProductId());
        assertEquals(quantity, item.getQuantity());
        assertEquals(price, item.getPrice());
        assertEquals(new BigDecimal("59.98"), item.getSubtotal());
    }

    @Test
    @DisplayName("Debería lanzar excepción si productId es null")
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Given
        String productId = null;
        Integer quantity = 1;
        BigDecimal price = new BigDecimal("10.00");

        // When & Then
        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, quantity, price);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción si productId está vacío")
    void shouldThrowExceptionWhenProductIdIsEmpty() {
        // Given
        String productId = "";
        Integer quantity = 1;
        BigDecimal price = new BigDecimal("10.00");

        // When & Then
        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, quantity, price);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción si quantity es null")
    void shouldThrowExceptionWhenQuantityIsNull() {
        // Given
        String productId = "PROD-001";
        Integer quantity = null;
        BigDecimal price = new BigDecimal("10.00");

        // When & Then
        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, quantity, price);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción si quantity es menor o igual a cero")
    void shouldThrowExceptionWhenQuantityIsZeroOrNegative() {
        // Given
        String productId = "PROD-001";
        BigDecimal price = new BigDecimal("10.00");

        // When & Then
        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, 0, price);
        });

        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, -1, price);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción si price es null")
    void shouldThrowExceptionWhenPriceIsNull() {
        // Given
        String productId = "PROD-001";
        Integer quantity = 1;
        BigDecimal price = null;

        // When & Then
        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, quantity, price);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción si price es negativo")
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Given
        String productId = "PROD-001";
        Integer quantity = 1;
        BigDecimal price = new BigDecimal("-10.00");

        // When & Then
        assertThrows(InvalidOrderItemException.class, () -> {
            new OrderItem(productId, quantity, price);
        });
    }

    @Test
    @DisplayName("Debería calcular correctamente el subtotal")
    void shouldCalculateSubtotalCorrectly() {
        // Given
        String productId = "PROD-001";
        Integer quantity = 3;
        BigDecimal price = new BigDecimal("15.50");

        // When
        OrderItem item = new OrderItem(productId, quantity, price);

        // Then
        BigDecimal expectedSubtotal = new BigDecimal("46.50");
        assertEquals(expectedSubtotal, item.getSubtotal());
    }

    @Test
    @DisplayName("Debería ser igual si tiene los mismos valores")
    void shouldBeEqualWhenSameValues() {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", 2, new BigDecimal("10.00"));
        OrderItem item2 = new OrderItem("PROD-001", 2, new BigDecimal("10.00"));

        // When & Then
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }
}
