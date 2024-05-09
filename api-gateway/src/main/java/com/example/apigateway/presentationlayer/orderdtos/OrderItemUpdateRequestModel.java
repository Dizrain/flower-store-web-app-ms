package com.example.apigateway.presentationlayer.orderdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class OrderItemUpdateRequestModel {
    private String productId;
    private int quantity;
}