package com.example.paymentservice.datamapperlayer;

import com.example.paymentservice.datalayer.Payment;
import com.example.paymentservice.presentationlayer.PaymentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PaymentResponseMapper {

    @Mapping(expression = "java(payment.getPaymentIdentifier().getPaymentId())", target = "paymentId")
    PaymentResponseModel entityToPaymentResponseModel(Payment payment);

    List<PaymentResponseModel> entityListToPaymentResponseModelList(List<Payment> payments);
}