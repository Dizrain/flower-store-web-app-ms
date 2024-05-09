package com.example.ordersservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseModel {

    private String orderId;
    private String status;
    private CustomerDetailsResponseModel customerDetails;
    private List<OrderItemResponseModel> items;
    private Double totalPrice;
}
