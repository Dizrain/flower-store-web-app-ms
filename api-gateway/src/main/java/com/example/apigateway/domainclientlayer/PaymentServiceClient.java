package com.example.apigateway.domainclientlayer;

import com.example.apigateway.utils.HttpErrorInfo;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
import com.example.apigateway.presentationlayer.paymentdtos.PaymentRequestModel;
import com.example.apigateway.presentationlayer.paymentdtos.PaymentResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String PAYMENT_SERVICE_BASE_URL;

    public PaymentServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${app.payments-service.host}") String paymentServiceHost, @Value("${app.payments-service.port}") String paymentServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.PAYMENT_SERVICE_BASE_URL = "http://" + paymentServiceHost + ":" + paymentServicePort + "/api/v1/payments";
    }

    public List<PaymentResponseModel> getAllPayments() {
        try {
            ResponseEntity<List<PaymentResponseModel>> paymentResponseModels = this.restTemplate.exchange(this.PAYMENT_SERVICE_BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<PaymentResponseModel>>() {});

            return paymentResponseModels.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public PaymentResponseModel getPaymentById(String paymentId) {
        try {
            PaymentResponseModel paymentResponseModel = this.restTemplate.getForObject(this.PAYMENT_SERVICE_BASE_URL + "/" + paymentId, PaymentResponseModel.class);
            return paymentResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel) {
        try {
            PaymentResponseModel paymentResponseModel = this.restTemplate.postForObject(this.PAYMENT_SERVICE_BASE_URL, paymentRequestModel, PaymentResponseModel.class);

            return paymentResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public PaymentResponseModel updatePayment(String paymentId, PaymentRequestModel paymentRequestModel) {
        try {
            HttpEntity<PaymentRequestModel> requestEntity = new HttpEntity<>(paymentRequestModel);
            ResponseEntity<PaymentResponseModel> responseEntity = restTemplate.exchange(this.PAYMENT_SERVICE_BASE_URL + "/" + paymentId, HttpMethod.PUT, requestEntity, PaymentResponseModel.class);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removePayment(String paymentId) {
        try {
            this.restTemplate.delete(this.PAYMENT_SERVICE_BASE_URL + "/" + paymentId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
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
            throw new InvalidInputException(getErrorMessage(ex));
        } else if (ex.getStatusCode() == NOT_FOUND) {
            throw new NotFoundException(getErrorMessage(ex));
        }

        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}