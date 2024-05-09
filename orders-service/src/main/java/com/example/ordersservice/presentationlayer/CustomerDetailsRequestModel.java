package com.example.ordersservice.presentationlayer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDetailsRequestModel {
    private String customerId;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
}