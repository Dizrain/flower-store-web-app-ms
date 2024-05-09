package com.example.ordersservice.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class OrderItemIdentifier {
    private String orderItemId;

    public OrderItemIdentifier() {
        // Generates a unique identifier upon creation
        this.orderItemId = UUID.randomUUID().toString();
    }

    public OrderItemIdentifier(String orderId) {
        // Allows setting a specific identifier, useful for data import or integration scenarios
        this.orderItemId = orderId;
    }
}
