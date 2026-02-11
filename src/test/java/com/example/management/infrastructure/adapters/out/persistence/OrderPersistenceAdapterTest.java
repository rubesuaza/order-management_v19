package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderEntity;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderItemEntity;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderStatusEntity;
import com.example.management.infrastructure.adapters.out.persistence.repository.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests de contrato para el adaptador de persistencia OrderPersistenceAdapter.
 */
@ExtendWith(MockitoExtension.class)
class OrderPersistenceAdapterTest {
    
    @Mock
    private OrderJpaRepository jpaRepository;
    
    @InjectMocks
    private OrderPersistenceAdapter adapter;
    
    private Order testOrder;
    private OrderEntity testOrderEntity;
    
    @BeforeEach
    void setUp() {
        OrderItem item1 = new OrderItem("product-1", 2, new BigDecimal("10.50"));
        OrderItem item2 = new OrderItem("product-2", 1, new BigDecimal("5.00"));
        
        testOrder = new Order("customer-123", Arrays.asList(item1, item2));
        
        testOrderEntity = new OrderEntity();
        testOrderEntity.setId(testOrder.getId());
        testOrderEntity.setCustomerId("customer-123");
        testOrderEntity.setStatus(OrderStatusEntity.PENDING);
        testOrderEntity.setCreatedAt(LocalDateTime.now());
        testOrderEntity.setUpdatedAt(LocalDateTime.now());
        
        // Agregar items a la entidad para que la conversión a dominio funcione
        OrderItemEntity itemEntity1 = new OrderItemEntity("product-1", 2, new BigDecimal("10.50"), testOrderEntity);
        OrderItemEntity itemEntity2 = new OrderItemEntity("product-2", 1, new BigDecimal("5.00"), testOrderEntity);
        testOrderEntity.addItem(itemEntity1);
        testOrderEntity.addItem(itemEntity2);
    }
    
    @Test
    void testSave() {
        // Given
        when(jpaRepository.save(any(OrderEntity.class))).thenReturn(testOrderEntity);
        
        // When
        Order savedOrder = adapter.save(testOrder);
        
        // Then
        assertNotNull(savedOrder);
        assertEquals(testOrder.getId(), savedOrder.getId());
        assertEquals(testOrder.getCustomerId(), savedOrder.getCustomerId());
        verify(jpaRepository, times(1)).save(any(OrderEntity.class));
    }
    
    @Test
    void testFindById_WhenExists() {
        // Given
        when(jpaRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrderEntity));
        
        // When
        Optional<Order> foundOrder = adapter.findById(testOrder.getId());
        
        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals(testOrder.getId(), foundOrder.get().getId());
        verify(jpaRepository, times(1)).findById(testOrder.getId());
    }
    
    @Test
    void testFindById_WhenNotExists() {
        // Given
        when(jpaRepository.findById("non-existent")).thenReturn(Optional.empty());
        
        // When
        Optional<Order> foundOrder = adapter.findById("non-existent");
        
        // Then
        assertFalse(foundOrder.isPresent());
        verify(jpaRepository, times(1)).findById("non-existent");
    }
    
    @Test
    void testFindByCustomerId() {
        // Given
        List<OrderEntity> entities = Arrays.asList(testOrderEntity);
        when(jpaRepository.findByCustomerId("customer-123")).thenReturn(entities);
        
        // When
        List<Order> orders = adapter.findByCustomerId("customer-123");
        
        // Then
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(jpaRepository, times(1)).findByCustomerId("customer-123");
    }
    
    @Test
    void testFindAll() {
        // Given
        List<OrderEntity> entities = Arrays.asList(testOrderEntity);
        when(jpaRepository.findAll()).thenReturn(entities);
        
        // When
        List<Order> orders = adapter.findAll();
        
        // Then
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(jpaRepository, times(1)).findAll();
    }
    
    @Test
    void testDeleteById() {
        // Given
        doNothing().when(jpaRepository).deleteById(testOrder.getId());
        
        // When
        adapter.deleteById(testOrder.getId());
        
        // Then
        verify(jpaRepository, times(1)).deleteById(testOrder.getId());
    }
    
    @Test
    void testExistsById_WhenExists() {
        // Given
        when(jpaRepository.existsById(testOrder.getId())).thenReturn(true);
        
        // When
        boolean exists = adapter.existsById(testOrder.getId());
        
        // Then
        assertTrue(exists);
        verify(jpaRepository, times(1)).existsById(testOrder.getId());
    }
    
    @Test
    void testExistsById_WhenNotExists() {
        // Given
        when(jpaRepository.existsById("non-existent")).thenReturn(false);
        
        // When
        boolean exists = adapter.existsById("non-existent");
        
        // Then
        assertFalse(exists);
        verify(jpaRepository, times(1)).existsById("non-existent");
    }
}
