package com.example.paymentservice.businesslayer;


import com.example.paymentservice.datalayer.Payment;
import com.example.paymentservice.datalayer.PaymentIdentifier;
import com.example.paymentservice.datalayer.PaymentRepository;
import com.example.paymentservice.datamapperlayer.PaymentRequestMapper;
import com.example.paymentservice.datamapperlayer.PaymentResponseMapper;
import com.example.paymentservice.presentationlayer.PaymentRequestModel;
import com.example.paymentservice.presentationlayer.PaymentResponseModel;
import com.example.paymentservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentResponseMapper paymentResponseMapper;
    private final PaymentRequestMapper paymentRequestMapper;


    public PaymentServiceImpl(PaymentRepository paymentRepository,
                             PaymentResponseMapper paymentResponseMapper,
                             PaymentRequestMapper paymentRequestMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentResponseMapper = paymentResponseMapper;
        this.paymentRequestMapper = paymentRequestMapper;
    }

    @Override
    public List<PaymentResponseModel> getPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentResponseMapper.entityListToPaymentResponseModelList(payments);
    }



    @Override
    public PaymentResponseModel getPaymentByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        if (payment == null) {
            throw new NotFoundException("Unknown paymentId: " + paymentId);
        }
        return paymentResponseMapper.entityToPaymentResponseModel(payment);
    }



    @Override
    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel) {


        Payment payment = paymentRequestMapper.requestModelToEntity(paymentRequestModel, new PaymentIdentifier());

        return paymentResponseMapper.entityToPaymentResponseModel(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseModel updatePayment(PaymentRequestModel paymentRequestModel, String paymentId) {

        Payment existingPayment = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        //check if a payment with the provided UUID exists in the system. If it doesn't, return null
        //later, when we implement exception handling, we'll return an exception
        if (existingPayment == null) {
            throw new NotFoundException("Unknown paymentId: " + paymentId);
        }

        Payment updatedPayment = paymentRequestMapper.requestModelToEntity(paymentRequestModel,
                existingPayment.getPaymentIdentifier());
        updatedPayment.setId(existingPayment.getId());

        Payment response = paymentRepository.save(updatedPayment);
        return paymentResponseMapper.entityToPaymentResponseModel(response);
    }

    @Override
    public void removePayment(String paymentId) {
        Payment existingPayment = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        if (existingPayment == null) {
            throw new NotFoundException("Unknown paymentId: " + paymentId);
        }

        paymentRepository.delete(existingPayment);
    }
}
