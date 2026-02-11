package com.example.management.application.services;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.InvalidOrderException;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("OrderService Tests")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @InjectMocks
    private OrderService orderService;
    
    private Order testOrder;
    private OrderItem testItem1;
    private OrderItem testItem2;
    
    @BeforeEach
    void setUp() {
        testItem1 = new OrderItem("PROD-001", 2, new BigDecimal("10.00"));
        testItem2 = new OrderItem("PROD-002", 1, new BigDecimal("20.00"));
        testOrder = new Order("CUST-001", Arrays.asList(testItem1, testItem2));
    }
    
    @Test
    @DisplayName("Debería crear una orden correctamente")
    void shouldCreateOrder() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        
        // When
        Order createdOrder = orderService.createOrder("CUST-001", Arrays.asList(testItem1, testItem2));
        
        // Then
        assertNotNull(createdOrder);
        assertEquals("CUST-001", createdOrder.getCustomerId());
        assertEquals(2, createdOrder.getItems().size());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería obtener una orden por ID cuando existe")
    void shouldGetOrderByIdWhenExists() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        
        // When
        Optional<Order> foundOrder = orderService.getOrderById(testOrder.getId());
        
        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals(testOrder.getId(), foundOrder.get().getId());
        verify(orderRepository, times(1)).findById(testOrder.getId());
    }
    
    @Test
    @DisplayName("Debería retornar Optional vacío cuando la orden no existe")
    void shouldReturnEmptyWhenOrderNotFound() {
        // Given
        when(orderRepository.findById("non-existent")).thenReturn(Optional.empty());
        
        // When
        Optional<Order> foundOrder = orderService.getOrderById("non-existent");
        
        // Then
        assertFalse(foundOrder.isPresent());
        verify(orderRepository, times(1)).findById("non-existent");
    }
    
    @Test
    @DisplayName("Debería obtener todas las órdenes de un cliente")
    void shouldGetOrdersByCustomerId() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerId("CUST-001")).thenReturn(orders);
        
        // When
        List<Order> foundOrders = orderService.getOrdersByCustomerId("CUST-001");
        
        // Then
        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
        assertEquals("CUST-001", foundOrders.get(0).getCustomerId());
        verify(orderRepository, times(1)).findByCustomerId("CUST-001");
    }
    
    @Test
    @DisplayName("Debería obtener todas las órdenes")
    void shouldGetAllOrders() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);
        
        // When
        List<Order> allOrders = orderService.getAllOrders();
        
        // Then
        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
        verify(orderRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Debería confirmar una orden correctamente")
    void shouldConfirmOrder() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Order confirmedOrder = orderService.confirmOrder(testOrder.getId());
        
        // Then
        assertEquals(OrderStatus.CONFIRMED, confirmedOrder.getStatus());
        verify(orderRepository, times(1)).findById(testOrder.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería lanzar excepción al confirmar orden inexistente")
    void shouldThrowExceptionWhenConfirmingNonExistentOrder() {
        // Given
        when(orderRepository.findById("non-existent")).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            orderService.confirmOrder("non-existent");
        });
        
        verify(orderRepository, times(1)).findById("non-existent");
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería cancelar una orden correctamente")
    void shouldCancelOrder() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Order cancelledOrder = orderService.cancelOrder(testOrder.getId());
        
        // Then
        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());
        verify(orderRepository, times(1)).findById(testOrder.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería lanzar excepción al cancelar orden inexistente")
    void shouldThrowExceptionWhenCancellingNonExistentOrder() {
        // Given
        when(orderRepository.findById("non-existent")).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            orderService.cancelOrder("non-existent");
        });
        
        verify(orderRepository, times(1)).findById("non-existent");
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería enviar una orden correctamente")
    void shouldShipOrder() {
        // Given
        testOrder.confirm();
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Order shippedOrder = orderService.shipOrder(testOrder.getId());
        
        // Then
        assertEquals(OrderStatus.SHIPPED, shippedOrder.getStatus());
        verify(orderRepository, times(1)).findById(testOrder.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería lanzar excepción al enviar orden inexistente")
    void shouldThrowExceptionWhenShippingNonExistentOrder() {
        // Given
        when(orderRepository.findById("non-existent")).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            orderService.shipOrder("non-existent");
        });
        
        verify(orderRepository, times(1)).findById("non-existent");
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería agregar un item a una orden correctamente")
    void shouldAddItemToOrder() {
        // Given
        OrderItem newItem = new OrderItem("PROD-003", 3, new BigDecimal("15.00"));
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Order updatedOrder = orderService.addItemToOrder(testOrder.getId(), newItem);
        
        // Then
        assertEquals(3, updatedOrder.getItems().size());
        verify(orderRepository, times(1)).findById(testOrder.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Debería lanzar excepción al agregar item a orden inexistente")
    void shouldThrowExceptionWhenAddingItemToNonExistentOrder() {
        // Given
        OrderItem newItem = new OrderItem("PROD-003", 3, new BigDecimal("15.00"));
        when(orderRepository.findById("non-existent")).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(InvalidOrderException.class, () -> {
            orderService.addItemToOrder("non-existent", newItem);
        });
        
        verify(orderRepository, times(1)).findById("non-existent");
        verify(orderRepository, never()).save(any(Order.class));
    }
}
