package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Entidad de dominio que representa una orden.
 * Encapsula la lógica de negocio relacionada con órdenes.
 */
public class Order {
    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String customerId, List<OrderItem> items) {
        validateCustomerId(customerId);
        validateItems(items);
        
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Método estático para reconstruir una orden desde la persistencia.
     * Permite establecer todos los campos incluyendo el ID existente.
     */
    public static Order reconstruct(String id, String customerId, List<OrderItem> items, 
                                   OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateCustomerId(customerId);
        validateItems(items);
        
        Order order = new Order(customerId, items);
        // Usar reflexión o crear un constructor package-private sería mejor,
        // pero por ahora usaremos un enfoque diferente: crear una clase interna
        // o modificar para permitir reconstrucción
        
        // Alternativa: crear un constructor package-private adicional
        return new Order(id, customerId, items, status, createdAt, updatedAt);
    }
    
    /**
     * Constructor package-private para reconstrucción desde persistencia.
     */
    Order(String id, String customerId, List<OrderItem> items, OrderStatus status, 
          LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidOrderException("El id no puede ser null o vacío");
        }
        validateCustomerId(customerId);
        validateItems(items);
        
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = status != null ? status : OrderStatus.PENDING;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    private static void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new InvalidOrderException("El customerId no puede ser null o vacío");
        }
    }

    private static void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("La orden debe tener al menos un item");
        }
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Calcula el total de la orden sumando los subtotales de todos los items.
     */
    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Confirma la orden, cambiando su estado a CONFIRMED.
     */
    public void confirm() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("No se puede confirmar una orden cancelada");
        }
        if (this.status == OrderStatus.SHIPPED) {
            throw new InvalidOrderException("La orden ya ha sido enviada");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Cancela la orden, cambiando su estado a CANCELLED.
     */
    public void cancel() {
        if (this.status == OrderStatus.SHIPPED) {
            throw new InvalidOrderException("No se puede cancelar una orden que ya ha sido enviada");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca la orden como enviada, cambiando su estado a SHIPPED.
     * Requiere que la orden esté en estado CONFIRMED.
     */
    public void ship() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new InvalidOrderException("Solo se pueden enviar órdenes confirmadas");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Agrega un item a la orden.
     */
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new InvalidOrderException("No se puede agregar un item null");
        }
        if (this.status == OrderStatus.CANCELLED || this.status == OrderStatus.SHIPPED) {
            throw new InvalidOrderException("No se pueden agregar items a una orden cancelada o enviada");
        }
        this.items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", status=" + status +
                ", total=" + getTotal() +
                ", itemsCount=" + items.size() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
