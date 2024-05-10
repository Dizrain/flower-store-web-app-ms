package com.example.ordersservice.domainclientlayer;


import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentRequestModel;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentResponseModel;
import com.example.ordersservice.utils.exceptions.InvalidInputException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.example.ordersservice.utils.HttpErrorInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class PaymentsServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String PAYMENT_SERVICE_BASE_URL;

    public PaymentsServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                 @Value("${app.payments-service.host}") String paymentServiceHost,
                                 @Value("${app.payments-service.port}") String paymenttServicePort) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        PAYMENT_SERVICE_BASE_URL = "http://" + paymentServiceHost + ":" +
                paymenttServicePort + "/api/v1/payments";
    }

    public PaymentResponseModel processPayment(PaymentRequestModel paymentRequestModel) {
        try {

            PaymentResponseModel paymentResponseModel = restTemplate.postForObject(PAYMENT_SERVICE_BASE_URL,
                    paymentRequestModel, PaymentResponseModel.class);

            return paymentResponseModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }

    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {

        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }

        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

}

