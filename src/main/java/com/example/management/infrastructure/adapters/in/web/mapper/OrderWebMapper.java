package com.example.management.infrastructure.adapters.in.web.mapper;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.infrastructure.adapters.in.web.dto.*;

import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs web.
 */
public class OrderWebMapper {
    
    /**
     * Convierte un CreateOrderRequest a una lista de OrderItem del dominio.
     */
    public static java.util.List<OrderItem> toDomainItems(CreateOrderRequest request) {
        return request.getItems().stream()
                .map(item -> new OrderItem(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Convierte un OrderItemRequest a un OrderItem del dominio.
     */
    public static OrderItem toDomainItem(OrderItemRequest request) {
        return new OrderItem(
                request.getProductId(),
                request.getQuantity(),
                request.getPrice()
        );
    }
    
    /**
     * Convierte una entidad de dominio Order a un OrderResponse.
     */
    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream()
                        .map(OrderWebMapper::toItemResponse)
                        .collect(Collectors.toList()),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
    
    /**
     * Convierte un OrderItem del dominio a un OrderItemResponse.
     */
    public static OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getQuantity(),
                item.getPrice(),
                item.getSubtotal()
        );
    }
}
