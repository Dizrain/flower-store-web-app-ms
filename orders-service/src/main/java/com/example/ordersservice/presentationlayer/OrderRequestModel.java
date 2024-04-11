package com.example.ordersservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRequestModel {

    String customerId;
    List<OrderItemModel> items; // This assumes the existence of an OrderItemModel class to capture item-specific data
    String shippingAddress;
    String billingInformation;

    @Value
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderItemModel {
        String productId;
        int quantity;
        double pricePerItem;
    }
}
