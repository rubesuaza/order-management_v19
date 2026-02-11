package com.example.management.application.services;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de órdenes.
 */
public class OrderService implements OrderUseCase {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    @Override
    public Order createOrder(String customerId, List<OrderItem> items) {
        Order order = new Order(customerId, items);
        return orderRepository.save(order);
    }
    
    @Override
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }
    
    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Override
    public Order confirmOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.confirm();
        return orderRepository.save(order);
    }
    
    @Override
    public Order cancelOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel();
        return orderRepository.save(order);
    }
    
    @Override
    public Order shipOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.ship();
        return orderRepository.save(order);
    }
    
    @Override
    public Order addItemToOrder(String orderId, OrderItem item) {
        Order order = findOrderOrThrow(orderId);
        order.addItem(item);
        return orderRepository.save(order);
    }
    
    private Order findOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidOrderException("Orden no encontrada con ID: " + orderId));
    }
}
