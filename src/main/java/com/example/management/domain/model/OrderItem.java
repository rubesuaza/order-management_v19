package com.example.management.domain.model;

import com.example.management.domain.exception.InvalidOrderItemException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object que representa un item de una orden.
 * Inmutable y valida sus invariantes en construcción.
 */
public class OrderItem {
    private final String productId;
    private final Integer quantity;
    private final BigDecimal price;

    public OrderItem(String productId, Integer quantity, BigDecimal price) {
        validateProductId(productId);
        validateQuantity(quantity);
        validatePrice(price);
        
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    private void validateProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new InvalidOrderItemException("El productId no puede ser null o vacío");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOrderItemException("La cantidad debe ser mayor a cero");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidOrderItemException("El precio no puede ser null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderItemException("El precio no puede ser negativo");
        }
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Calcula el subtotal del item (quantity * price)
     */
    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productId, orderItem.productId) &&
               Objects.equals(quantity, orderItem.quantity) &&
               Objects.equals(price, orderItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, price);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
