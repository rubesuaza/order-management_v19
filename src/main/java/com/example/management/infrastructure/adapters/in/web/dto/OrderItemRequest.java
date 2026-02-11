package com.example.management.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO para un item de orden en las peticiones.
 */
public class OrderItemRequest {
    
    @NotBlank(message = "El productId es obligatorio")
    private final String productId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private final Integer quantity;
    
    @NotNull(message = "El precio es obligatorio")
    private final BigDecimal price;
    
    public OrderItemRequest(String productId, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
}
