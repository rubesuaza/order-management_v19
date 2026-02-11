package com.example.management.infrastructure.adapters.out.persistence.mapper;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderEntity;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderItemEntity;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderStatusEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y entidades JPA.
 */
public class OrderMapper {
    
    /**
     * Convierte una entidad de dominio Order a una entidad JPA OrderEntity.
     */
    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity(
                order.getId(),
                order.getCustomerId(),
                toEntityStatus(order.getStatus()),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
        
        // Mapear items
        order.getItems().forEach(item -> {
            OrderItemEntity itemEntity = new OrderItemEntity(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice(),
                    entity
            );
            entity.getItems().add(itemEntity);
        });
        
        return entity;
    }
    
    /**
     * Convierte una entidad JPA OrderEntity a una entidad de dominio Order.
     */
    public static Order toDomain(OrderEntity entity) {
        List<OrderItem> domainItems = entity.getItems().stream()
                .map(OrderMapper::toDomainItem)
                .collect(Collectors.toList());
        
        OrderStatus domainStatus = toDomainStatus(entity.getStatus());
        
        // Usar el método de reconstrucción para crear la orden con todos los datos
        return Order.reconstruct(
                entity.getId(),
                entity.getCustomerId(),
                domainItems,
                domainStatus,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
    private static OrderItem toDomainItem(OrderItemEntity entity) {
        return new OrderItem(
                entity.getProductId(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }
    
    private static OrderStatusEntity toEntityStatus(OrderStatus status) {
        return switch (status) {
            case PENDING -> OrderStatusEntity.PENDING;
            case CONFIRMED -> OrderStatusEntity.CONFIRMED;
            case SHIPPED -> OrderStatusEntity.SHIPPED;
            case CANCELLED -> OrderStatusEntity.CANCELLED;
        };
    }
    
    private static OrderStatus toDomainStatus(OrderStatusEntity status) {
        return switch (status) {
            case PENDING -> OrderStatus.PENDING;
            case CONFIRMED -> OrderStatus.CONFIRMED;
            case SHIPPED -> OrderStatus.SHIPPED;
            case CANCELLED -> OrderStatus.CANCELLED;
        };
    }
}
