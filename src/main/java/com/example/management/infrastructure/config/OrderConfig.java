package com.example.management.infrastructure.config;

import com.example.management.application.ports.in.OrderUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para la inyección de dependencias.
 * Conecta los puertos con sus implementaciones (adaptadores).
 */
@Configuration
public class OrderConfig {
    
    /**
     * Crea el servicio de aplicación que implementa OrderUseCase.
     * Spring inyectará automáticamente el OrderRepository (OrderPersistenceAdapter).
     */
    @Bean
    public OrderUseCase orderUseCase(OrderRepository orderRepository) {
        return new OrderService(orderRepository);
    }
}
