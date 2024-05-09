package com.example.apigateway.presentationlayer.customerdtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Jacksonized
public class CustomerRequestModel {

    String name;
    String email; // Assuming each customer has a unique email
    String contactNumber; // Contact number of the customer
    String address; // Address for delivery or contact purposes
}
