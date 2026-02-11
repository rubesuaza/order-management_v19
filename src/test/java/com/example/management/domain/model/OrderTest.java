package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order Tests")
class OrderTest {

    @Test
    @DisplayName("Should create a valid Order")
    void shouldCreateValidOrder() {
        // Given
        String customerId = "CUST-001";
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", 2, new BigDecimal("10.00")),
            new OrderItem("PROD-002", 1, new BigDecimal("20.00"))
        );

        // When
        Order order = new Order(customerId, items);

        // Then
        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("40.00"), order.getTotal());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception if customerId is null")
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        // Given
        String customerId = null;
        List<OrderItem> items = List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00")));

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            new Order(customerId, items);
        });
    }

    @Test
    @DisplayName("Should throw exception if customerId is empty")
    void shouldThrowExceptionWhenCustomerIdIsEmpty() {
        // Given
        String customerId = "";
        List<OrderItem> items = List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00")));

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            new Order(customerId, items);
        });
    }

    @Test
    @DisplayName("Should throw exception if items is null")
    void shouldThrowExceptionWhenItemsIsNull() {
        // Given
        String customerId = "CUST-001";
        List<OrderItem> items = null;

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            new Order(customerId, items);
        });
    }

    @Test
    @DisplayName("Should throw exception if items is empty")
    void shouldThrowExceptionWhenItemsIsEmpty() {
        // Given
        String customerId = "CUST-001";
        List<OrderItem> items = new ArrayList<>();

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            new Order(customerId, items);
        });
    }

    @Test
    @DisplayName("Should calculate total correctly")
    void shouldCalculateTotalCorrectly() {
        // Given
        String customerId = "CUST-001";
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", 3, new BigDecimal("15.50")),
            new OrderItem("PROD-002", 2, new BigDecimal("25.00")),
            new OrderItem("PROD-003", 1, new BigDecimal("10.00"))
        );

        // When
        Order order = new Order(customerId, items);

        // Then
        BigDecimal expectedTotal = new BigDecimal("106.50"); // 46.50 + 50.00 + 10.00
        assertEquals(0, expectedTotal.compareTo(order.getTotal()), 
            "El total esperado es " + expectedTotal + " pero se obtuvo " + order.getTotal());
    }

    @Test
    @DisplayName("Should change status to CONFIRMED")
    void shouldChangeStatusToConfirmed() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));

        // When
        order.confirm();

        // Then
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("Should change status to CANCELLED")
    void shouldChangeStatusToCancelled() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));

        // When
        order.cancel();

        // Then
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("Should change status to SHIPPED")
    void shouldChangeStatusToShipped() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        order.confirm();

        // When
        order.ship();

        // Then
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    @DisplayName("Should not allow ship without confirmation")
    void shouldNotAllowShipWithoutConfirmation() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.ship();
        });
    }

    @Test
    @DisplayName("No debería permitir cancelar una orden ya enviada")
    void shouldNotAllowCancelShippedOrder() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        order.confirm();
        order.ship();

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.cancel();
        });
    }

    @Test
    @DisplayName("Debería actualizar updatedAt al cambiar estado")
    void shouldUpdateUpdatedAtWhenStatusChanges() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        LocalDateTime initialUpdatedAt = order.getUpdatedAt();

        // When
        try {
            Thread.sleep(10); // Pequeña pausa para asegurar diferencia de tiempo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        order.confirm();

        // Then
        assertTrue(order.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    @DisplayName("Debería agregar un item a la orden")
    void shouldAddItemToOrder() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        OrderItem newItem = new OrderItem("PROD-002", 2, new BigDecimal("15.00"));
        BigDecimal initialTotal = order.getTotal();

        // When
        order.addItem(newItem);

        // Then
        assertEquals(2, order.getItems().size());
        BigDecimal expectedTotal = initialTotal.add(newItem.getSubtotal());
        assertEquals(0, expectedTotal.compareTo(order.getTotal()),
            "El total esperado es " + expectedTotal + " pero se obtuvo " + order.getTotal());
    }

    @Test
    @DisplayName("No debería permitir agregar item null")
    void shouldNotAllowAddNullItem() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.addItem(null);
        });
    }

    @Test
    @DisplayName("No debería permitir confirmar una orden cancelada")
    void shouldNotAllowConfirmCancelledOrder() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        order.cancel();

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.confirm();
        });
    }

    @Test
    @DisplayName("No debería permitir confirmar una orden ya enviada")
    void shouldNotAllowConfirmShippedOrder() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        order.confirm();
        order.ship();

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.confirm();
        });
    }

    @Test
    @DisplayName("No debería permitir agregar items a una orden cancelada")
    void shouldNotAllowAddItemToCancelledOrder() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        order.cancel();
        OrderItem newItem = new OrderItem("PROD-002", 2, new BigDecimal("15.00"));

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.addItem(newItem);
        });
    }

    @Test
    @DisplayName("No debería permitir agregar items a una orden enviada")
    void shouldNotAllowAddItemToShippedOrder() {
        // Given
        Order order = new Order("CUST-001", 
            List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00"))));
        order.confirm();
        order.ship();
        OrderItem newItem = new OrderItem("PROD-002", 2, new BigDecimal("15.00"));

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            order.addItem(newItem);
        });
    }

    @Test
    @DisplayName("Debería reconstruir una orden desde persistencia correctamente")
    void shouldReconstructOrderFromPersistence() {
        // Given
        String id = "ORDER-123";
        String customerId = "CUST-001";
        List<OrderItem> items = List.of(
            new OrderItem("PROD-001", 2, new BigDecimal("10.00")),
            new OrderItem("PROD-002", 1, new BigDecimal("20.00"))
        );
        OrderStatus status = OrderStatus.CONFIRMED;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        Order reconstructedOrder = Order.reconstruct(id, customerId, items, status, createdAt, updatedAt);

        // Then
        assertNotNull(reconstructedOrder);
        assertEquals(id, reconstructedOrder.getId());
        assertEquals(customerId, reconstructedOrder.getCustomerId());
        assertEquals(status, reconstructedOrder.getStatus());
        assertEquals(createdAt, reconstructedOrder.getCreatedAt());
        assertEquals(updatedAt, reconstructedOrder.getUpdatedAt());
        assertEquals(2, reconstructedOrder.getItems().size());
        assertEquals(new BigDecimal("40.00"), reconstructedOrder.getTotal());
    }

    @Test
    @DisplayName("Debería lanzar excepción al reconstruir orden con id null o vacío")
    void shouldThrowExceptionWhenReconstructingWithNullOrEmptyId() {
        // Given
        String customerId = "CUST-001";
        List<OrderItem> items = List.of(new OrderItem("PROD-001", 1, new BigDecimal("10.00")));
        OrderStatus status = OrderStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            Order.reconstruct(null, customerId, items, status, createdAt, updatedAt);
        });

        assertThrows(InvalidOrderException.class, () -> {
            Order.reconstruct("", customerId, items, status, createdAt, updatedAt);
        });
    }
}
