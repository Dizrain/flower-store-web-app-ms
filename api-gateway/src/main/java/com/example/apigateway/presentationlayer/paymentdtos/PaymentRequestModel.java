package com.example.apigateway.presentationlayer.paymentdtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequestModel {


    String paymentId;
    double amount;
    LocalDateTime paymentDate;
    PaymentMethod paymentMethod;

}
