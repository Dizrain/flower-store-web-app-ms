package com.example.customersservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequestModel {

    String name;
    String email; // Assuming each customer has a unique email
    String contactNumber; // Contact number of the customer
    String address; // Address for delivery or contact purposes
}
