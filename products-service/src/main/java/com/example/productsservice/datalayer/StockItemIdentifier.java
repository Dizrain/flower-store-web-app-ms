package com.example.productsservice.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class StockItemIdentifier {
    private String stockItemId; // Unique identifier for the stock item

    public StockItemIdentifier() {
        // Generate a unique identifier using UUID
        this.stockItemId = UUID.randomUUID().toString();
    }

    public StockItemIdentifier(String stockItemId) {
        // Allow setting a custom identifier if needed
        this.stockItemId = stockItemId;
    }
}
