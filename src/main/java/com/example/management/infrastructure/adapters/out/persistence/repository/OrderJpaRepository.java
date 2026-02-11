package com.example.management.infrastructure.adapters.out.persistence.repository;

import com.example.management.infrastructure.adapters.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad OrderEntity.
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
    
    /**
     * Busca todas las órdenes de un cliente.
     * @param customerId ID del cliente
     * @return Lista de órdenes del cliente
     */
    List<OrderEntity> findByCustomerId(String customerId);
}
