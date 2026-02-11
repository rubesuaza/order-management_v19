package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderEntity;
import com.example.management.infrastructure.adapters.out.persistence.mapper.OrderMapper;
import com.example.management.infrastructure.adapters.out.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia que implementa el puerto OrderRepository.
 * Conecta la capa de aplicación con la capa de infraestructura JPA.
 */
@Component
public class OrderPersistenceAdapter implements OrderRepository {
    
    private final OrderJpaRepository jpaRepository;
    
    public OrderPersistenceAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderMapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return OrderMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Order> findById(String orderId) {
        return jpaRepository.findById(orderId)
                .map(OrderMapper::toDomain);
    }
    
    @Override
    public List<Order> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(String orderId) {
        jpaRepository.deleteById(orderId);
    }
    
    @Override
    public boolean existsById(String orderId) {
        return jpaRepository.existsById(orderId);
    }
}
