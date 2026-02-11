package com.example.management.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden.
 */
public class OrderResponse {
    
    private String id;
    private String customerId;
    private List<OrderItemResponse> items;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public OrderResponse() {
    }
    
    public OrderResponse(String id, String customerId, List<OrderItemResponse> items, 
                        String status, BigDecimal total, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<OrderItemResponse> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
