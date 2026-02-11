package com.example.management.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO para la creación de una orden.
 */
public class CreateOrderRequest {
    
    @NotBlank(message = "El customerId es obligatorio")
    private String customerId;
    
    @NotEmpty(message = "La orden debe tener al menos un item")
    private List<OrderItemRequest> items;
    
    public CreateOrderRequest() {
    }
    
    public CreateOrderRequest(String customerId, List<OrderItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<OrderItemRequest> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
