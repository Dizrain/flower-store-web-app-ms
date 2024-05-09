package com.example.ordersservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderRequestModel {
    private CustomerDetailsRequestModel customerDetails;

    private List<OrderItemRequestModel> items;
}