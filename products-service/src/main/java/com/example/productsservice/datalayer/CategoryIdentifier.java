package com.example.productsservice.datalayer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class CategoryIdentifier {

    private String categoryId;

    public CategoryIdentifier() {
        this.categoryId = UUID.randomUUID().toString();
    }

    public CategoryIdentifier(String categoryId) {
        this.categoryId = categoryId;
    }
}
