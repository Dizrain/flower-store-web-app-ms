package com.example.ordersservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemRequestModel {

    private String productId;
    private int quantity;
    private double price;
}
