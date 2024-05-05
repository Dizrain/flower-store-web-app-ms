package com.example.paymentservice.businesslayer;




import com.example.paymentservice.presentationlayer.PaymentRequestModel;
import com.example.paymentservice.presentationlayer.PaymentResponseModel;

import java.util.List;

public interface PaymentService {

    List<PaymentResponseModel> getPayments();
    PaymentResponseModel getPaymentByPaymentId(String paymentId);
    PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel);
    PaymentResponseModel updatePayment(PaymentRequestModel updatedPayment, String paymentId);
    void removePayment(String paymentId);
}
