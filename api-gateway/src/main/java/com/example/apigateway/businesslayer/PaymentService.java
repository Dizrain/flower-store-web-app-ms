package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.paymentdtos.PaymentRequestModel;
import com.example.apigateway.presentationlayer.paymentdtos.PaymentResponseModel;

import java.util.List;

public interface PaymentService {

    List<PaymentResponseModel> getPayments();
    PaymentResponseModel getPaymentByPaymentId(String paymentId);
    PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel);
    PaymentResponseModel updatePayment(PaymentRequestModel updatedPayment, String paymentId);
    void removePayment(String paymentId);
}
