package com.example.apigateway.presentationlayer.orderdtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Jacksonized
@Builder
public class OrderRequestModel {
    private CustomerDetailsRequestModel customerDetails;

    private List<OrderItemRequestModel> items;
}