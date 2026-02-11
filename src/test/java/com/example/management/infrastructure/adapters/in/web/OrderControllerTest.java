package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.infrastructure.adapters.in.web.dto.CreateOrderRequest;
import com.example.management.infrastructure.adapters.in.web.dto.OrderItemRequest;
import com.example.management.infrastructure.adapters.in.web.dto.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de contrato para el controlador REST OrderController.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OrderUseCase orderUseCase;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Order testOrder;
    private CreateOrderRequest createOrderRequest;
    
    @BeforeEach
    void setUp() {
        OrderItem item1 = new OrderItem("product-1", 2, new BigDecimal("10.50"));
        OrderItem item2 = new OrderItem("product-2", 1, new BigDecimal("5.00"));
        
        testOrder = new Order("customer-123", Arrays.asList(item1, item2));
        
        OrderItemRequest itemRequest1 = new OrderItemRequest("product-1", 2, new BigDecimal("10.50"));
        OrderItemRequest itemRequest2 = new OrderItemRequest("product-2", 1, new BigDecimal("5.00"));
        
        createOrderRequest = new CreateOrderRequest("customer-123", Arrays.asList(itemRequest1, itemRequest2));
    }
    
    @Test
    void testCreateOrder() throws Exception {
        // Given
        when(orderUseCase.createOrder(eq("customer-123"), any())).thenReturn(testOrder);
        
        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerId").value("customer-123"))
                .andExpect(jsonPath("$.status").value("PENDING"));
        
        verify(orderUseCase, times(1)).createOrder(eq("customer-123"), any());
    }
    
    @Test
    void testCreateOrder_InvalidRequest() throws Exception {
        // Given
        CreateOrderRequest invalidRequest = new CreateOrderRequest("", Arrays.asList());
        
        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(orderUseCase, never()).createOrder(any(), any());
    }
    
    @Test
    void testGetOrderById_WhenExists() throws Exception {
        // Given
        when(orderUseCase.getOrderById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        
        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId()))
                .andExpect(jsonPath("$.customerId").value("customer-123"));
        
        verify(orderUseCase, times(1)).getOrderById(testOrder.getId());
    }
    
    @Test
    void testGetOrderById_WhenNotExists() throws Exception {
        // Given
        when(orderUseCase.getOrderById("non-existent")).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", "non-existent"))
                .andExpect(status().isNotFound());
        
        verify(orderUseCase, times(1)).getOrderById("non-existent");
    }
    
    @Test
    void testGetOrdersByCustomerId() throws Exception {
        // Given
        when(orderUseCase.getOrdersByCustomerId("customer-123")).thenReturn(Arrays.asList(testOrder));
        
        // When & Then
        mockMvc.perform(get("/api/orders/customer/{customerId}", "customer-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerId").value("customer-123"));
        
        verify(orderUseCase, times(1)).getOrdersByCustomerId("customer-123");
    }
    
    @Test
    void testGetAllOrders() throws Exception {
        // Given
        when(orderUseCase.getAllOrders()).thenReturn(Arrays.asList(testOrder));
        
        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(orderUseCase, times(1)).getAllOrders();
    }
    
    @Test
    void testConfirmOrder() throws Exception {
        // Given
        Order confirmedOrder = Order.reconstruct(
            testOrder.getId(),
            testOrder.getCustomerId(),
            testOrder.getItems(),
            com.example.management.domain.model.OrderStatus.CONFIRMED,
            testOrder.getCreatedAt(),
            java.time.LocalDateTime.now()
        );
        when(orderUseCase.confirmOrder(testOrder.getId())).thenReturn(confirmedOrder);
        
        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/confirm", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
        
        verify(orderUseCase, times(1)).confirmOrder(testOrder.getId());
    }
    
    @Test
    void testConfirmOrder_InvalidState() throws Exception {
        // Given
        when(orderUseCase.confirmOrder(testOrder.getId()))
                .thenThrow(new InvalidOrderException("No se puede confirmar"));
        
        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/confirm", testOrder.getId()))
                .andExpect(status().isBadRequest());
        
        verify(orderUseCase, times(1)).confirmOrder(testOrder.getId());
    }
    
    @Test
    void testCancelOrder() throws Exception {
        // Given
        Order cancelledOrder = Order.reconstruct(
            testOrder.getId(),
            testOrder.getCustomerId(),
            testOrder.getItems(),
            com.example.management.domain.model.OrderStatus.CANCELLED,
            testOrder.getCreatedAt(),
            java.time.LocalDateTime.now()
        );
        when(orderUseCase.cancelOrder(testOrder.getId())).thenReturn(cancelledOrder);
        
        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/cancel", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
        
        verify(orderUseCase, times(1)).cancelOrder(testOrder.getId());
    }
    
    @Test
    void testShipOrder() throws Exception {
        // Given
        Order shippedOrder = Order.reconstruct(
            testOrder.getId(),
            testOrder.getCustomerId(),
            testOrder.getItems(),
            com.example.management.domain.model.OrderStatus.SHIPPED,
            testOrder.getCreatedAt(),
            java.time.LocalDateTime.now()
        );
        when(orderUseCase.shipOrder(testOrder.getId())).thenReturn(shippedOrder);
        
        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/ship", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
        
        verify(orderUseCase, times(1)).shipOrder(testOrder.getId());
    }
    
    @Test
    void testAddItemToOrder() throws Exception {
        // Given
        OrderItemRequest itemRequest = new OrderItemRequest("product-3", 1, new BigDecimal("15.00"));
        OrderItem newItem = new OrderItem("product-3", 1, new BigDecimal("15.00"));
        List<OrderItem> itemsWithNewItem = new ArrayList<>(testOrder.getItems());
        itemsWithNewItem.add(newItem);
        Order orderWithNewItem = Order.reconstruct(
            testOrder.getId(),
            testOrder.getCustomerId(),
            itemsWithNewItem,
            testOrder.getStatus(),
            testOrder.getCreatedAt(),
            java.time.LocalDateTime.now()
        );
        
        when(orderUseCase.addItemToOrder(eq(testOrder.getId()), any())).thenReturn(orderWithNewItem);
        
        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/items", testOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
        
        verify(orderUseCase, times(1)).addItemToOrder(eq(testOrder.getId()), any());
    }
}
