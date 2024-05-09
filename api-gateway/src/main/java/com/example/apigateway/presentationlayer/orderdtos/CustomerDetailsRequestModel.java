package com.example.apigateway.presentationlayer.orderdtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CustomerDetailsRequestModel {
    private String customerId;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
}