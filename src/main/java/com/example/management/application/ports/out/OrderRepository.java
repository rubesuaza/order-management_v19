package com.example.management.application.ports.out;

import com.example.management.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (repositorio) para la persistencia de órdenes.
 * Define las operaciones de acceso a datos que los adaptadores deben implementar.
 */
public interface OrderRepository {
    
    /**
     * Guarda una orden.
     * @param order Orden a guardar
     * @return La orden guardada
     */
    Order save(Order order);
    
    /**
     * Busca una orden por su ID.
     * @param orderId ID de la orden
     * @return La orden si existe, vacío en caso contrario
     */
    Optional<Order> findById(String orderId);
    
    /**
     * Busca todas las órdenes de un cliente.
     * @param customerId ID del cliente
     * @return Lista de órdenes del cliente
     */
    List<Order> findByCustomerId(String customerId);
    
    /**
     * Obtiene todas las órdenes.
     * @return Lista de todas las órdenes
     */
    List<Order> findAll();
    
    /**
     * Elimina una orden por su ID.
     * @param orderId ID de la orden a eliminar
     */
    void deleteById(String orderId);
    
    /**
     * Verifica si existe una orden con el ID dado.
     * @param orderId ID de la orden
     * @return true si existe, false en caso contrario
     */
    boolean existsById(String orderId);
}
