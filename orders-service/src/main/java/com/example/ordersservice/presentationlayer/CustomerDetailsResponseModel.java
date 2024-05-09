package com.example.ordersservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailsResponseModel {
    private String customerId;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
}