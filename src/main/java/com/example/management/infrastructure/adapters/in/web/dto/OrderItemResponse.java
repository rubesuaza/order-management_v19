package com.example.management.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

/**
 * DTO para un item de orden en las respuestas.
 */
public class OrderItemResponse {
    
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    
    public OrderItemResponse() {
    }
    
    public OrderItemResponse(String productId, Integer quantity, BigDecimal price, BigDecimal subtotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
