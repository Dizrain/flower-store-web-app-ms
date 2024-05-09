package com.example.apigateway.presentationlayer.productdtos;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class CustomerIdentifier {
    private String customerId;
    public CustomerIdentifier() {
        this.customerId = UUID.randomUUID().toString();
    }
    public CustomerIdentifier(String customerId) {
        this.customerId = customerId;
    }
}

