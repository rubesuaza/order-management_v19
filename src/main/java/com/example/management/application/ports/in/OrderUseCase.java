package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada (caso de uso) para la gestión de órdenes.
 * Define las operaciones que la aplicación puede realizar sobre las órdenes.
 */
public interface OrderUseCase {
    
    /**
     * Crea una nueva orden.
     * @param customerId ID del cliente
     * @param items Lista de items de la orden
     * @return La orden creada
     */
    Order createOrder(String customerId, List<com.example.management.domain.model.OrderItem> items);
    
    /**
     * Obtiene una orden por su ID.
     * @param orderId ID de la orden
     * @return La orden si existe, vacío en caso contrario
     */
    Optional<Order> getOrderById(String orderId);
    
    /**
     * Obtiene todas las órdenes de un cliente.
     * @param customerId ID del cliente
     * @return Lista de órdenes del cliente
     */
    List<Order> getOrdersByCustomerId(String customerId);
    
    /**
     * Obtiene todas las órdenes.
     * @return Lista de todas las órdenes
     */
    List<Order> getAllOrders();
    
    /**
     * Confirma una orden.
     * @param orderId ID de la orden a confirmar
     * @return La orden confirmada
     */
    Order confirmOrder(String orderId);
    
    /**
     * Cancela una orden.
     * @param orderId ID de la orden a cancelar
     * @return La orden cancelada
     */
    Order cancelOrder(String orderId);
    
    /**
     * Marca una orden como enviada.
     * @param orderId ID de la orden a enviar
     * @return La orden enviada
     */
    Order shipOrder(String orderId);
    
    /**
     * Agrega un item a una orden existente.
     * @param orderId ID de la orden
     * @param item Item a agregar
     * @return La orden actualizada
     */
    Order addItemToOrder(String orderId, com.example.management.domain.model.OrderItem item);
}
