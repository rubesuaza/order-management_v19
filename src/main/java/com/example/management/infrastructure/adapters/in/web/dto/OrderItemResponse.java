package com.example.management.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

/**
 * DTO para un item de orden en las respuestas.
 */
public class OrderItemResponse {
    
    private final String productId;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;
    
    public OrderItemResponse(String productId, Integer quantity, BigDecimal price, BigDecimal subtotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
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
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
