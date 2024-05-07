package com.example.ordersservice.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class ProductIdentifier {
    private String productId;
    public ProductIdentifier() {
        this.productId = UUID.randomUUID().toString();
    }
    public ProductIdentifier(String productId) {
        this.productId = productId;
    }
}
