package com.example.ordersservice.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class OrderIdentifier {
    private String orderId;

    public OrderIdentifier() {
        // Generates a unique identifier upon creation
        this.orderId = UUID.randomUUID().toString();
    }

    public OrderIdentifier(String orderId) {
        // Allows setting a specific identifier, useful for data import or integration scenarios
        this.orderId = orderId;
    }
}
