package com.example.paymentservice.presentationlayer;

import com.example.paymentservice.datalayer.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseModel {
    String paymentId;
    double amount;
    LocalDateTime paymentDate;
    PaymentMethod paymentMethod;
}
