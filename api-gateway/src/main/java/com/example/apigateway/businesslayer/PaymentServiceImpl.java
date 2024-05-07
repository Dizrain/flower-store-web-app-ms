package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.paymentdtos.PaymentRequestModel;
import com.example.apigateway.presentationlayer.paymentdtos.PaymentResponseModel;
import com.example.apigateway.domainclientlayer.PaymentServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentServiceClient paymentServiceClient;

    @Override
    public List<PaymentResponseModel> getPayments() {
        return paymentServiceClient.getAllPayments();
    }

    @Override
    public PaymentResponseModel getPaymentByPaymentId(String paymentId) {
        PaymentResponseModel payment = paymentServiceClient.getPaymentById(paymentId);

        return payment;
    }

    @Override
    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel) {
        return paymentServiceClient.addPayment(paymentRequestModel);
    }

    @Override
    public PaymentResponseModel updatePayment(PaymentRequestModel paymentRequestModel, String paymentId) {
        return paymentServiceClient.updatePayment(paymentId, paymentRequestModel);
    }

    @Override
    public void removePayment(String paymentId) {
        paymentServiceClient.removePayment(paymentId);
    }
}
