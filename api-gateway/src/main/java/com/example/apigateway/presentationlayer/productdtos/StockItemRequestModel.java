package com.example.apigateway.presentationlayer.productdtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Jacksonized
public class StockItemRequestModel {

    String productId; // The unique identifier of the product this stock item corresponds to
    int stockLevel; // The current stock level of the product
    int reorderThreshold; // The stock level at which a reorder is triggered
}