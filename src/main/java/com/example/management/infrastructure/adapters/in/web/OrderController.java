package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.model.Order;
import com.example.management.infrastructure.adapters.in.web.dto.CreateOrderRequest;
import com.example.management.infrastructure.adapters.in.web.dto.OrderItemRequest;
import com.example.management.infrastructure.adapters.in.web.dto.OrderResponse;
import com.example.management.infrastructure.adapters.in.web.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST que actúa como adaptador de entrada para la gestión de órdenes.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderUseCase orderUseCase;
    
    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }
    
    /**
     * Crea una nueva orden.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        List<com.example.management.domain.model.OrderItem> items = OrderWebMapper.toDomainItems(request);
        Order order = orderUseCase.createOrder(request.getCustomerId(), items);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderWebMapper.toResponse(order));
    }
    
    /**
     * Obtiene una orden por su ID.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        return orderUseCase.getOrderById(orderId)
                .map(order -> ResponseEntity.ok(OrderWebMapper.toResponse(order)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las órdenes de un cliente.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerId(@PathVariable String customerId) {
        List<OrderResponse> orders = orderUseCase.getOrdersByCustomerId(customerId).stream()
                .map(OrderWebMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Obtiene todas las órdenes.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderUseCase.getAllOrders().stream()
                .map(OrderWebMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Confirma una orden.
     */
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        try {
            Order order = orderUseCase.confirmOrder(orderId);
            return ResponseEntity.ok(OrderWebMapper.toResponse(order));
        } catch (InvalidOrderException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancela una orden.
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        try {
            Order order = orderUseCase.cancelOrder(orderId);
            return ResponseEntity.ok(OrderWebMapper.toResponse(order));
        } catch (InvalidOrderException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Marca una orden como enviada.
     */
    @PostMapping("/{orderId}/ship")
    public ResponseEntity<OrderResponse> shipOrder(@PathVariable String orderId) {
        try {
            Order order = orderUseCase.shipOrder(orderId);
            return ResponseEntity.ok(OrderWebMapper.toResponse(order));
        } catch (InvalidOrderException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Agrega un item a una orden existente.
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItemToOrder(
            @PathVariable String orderId,
            @Valid @RequestBody OrderItemRequest itemRequest) {
        try {
            com.example.management.domain.model.OrderItem item = OrderWebMapper.toDomainItem(itemRequest);
            Order order = orderUseCase.addItemToOrder(orderId, item);
            return ResponseEntity.ok(OrderWebMapper.toResponse(order));
        } catch (InvalidOrderException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
