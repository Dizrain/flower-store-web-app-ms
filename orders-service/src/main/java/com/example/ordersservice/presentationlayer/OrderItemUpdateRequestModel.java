package com.example.ordersservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderItemUpdateRequestModel {
    private String itemId;
    private String productId;
    private int quantity;
}