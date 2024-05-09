package com.example.apigateway.presentationlayer.orderdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseModel {

    private String orderItemId;
    private String productId;
    private int quantity;
    private double price;
}