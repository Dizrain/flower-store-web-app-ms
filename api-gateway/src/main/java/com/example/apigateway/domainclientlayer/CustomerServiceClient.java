package com.example.apigateway.domainclientlayer;

import com.example.apigateway.utils.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import com.example.apigateway.presentationlayer.customerdtos.*;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class CustomerServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String CUSTOMER_SERVICE_BASE_URL;

    public CustomerServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${app.customers-service.host}") String customersServiceHost, @Value("${app.customers-service.port}") String customersServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.CUSTOMER_SERVICE_BASE_URL = "http://" + customersServiceHost + ":" + customersServicePort + "/api/v1/customers";
    }

    public List<CustomerResponseModel> getAllCustomers() {

        try {
            ResponseEntity<List<CustomerResponseModel>> customerResponseModels = this.restTemplate.exchange(this.CUSTOMER_SERVICE_BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<CustomerResponseModel>>() {
            });

            return customerResponseModels.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public CustomerResponseModel getCustomerById(String customerId) {

        try {
            CustomerResponseModel customerResponseModel = this.restTemplate.getForObject(this.CUSTOMER_SERVICE_BASE_URL + "/" + customerId, CustomerResponseModel.class);
            return customerResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }


    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        try {
            CustomerResponseModel customerResponseModel = this.restTemplate.postForObject(this.CUSTOMER_SERVICE_BASE_URL, customerRequestModel, CustomerResponseModel.class);

            return customerResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }

    }

    public CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel) {
        try {
            HttpEntity<CustomerRequestModel> requestEntity = new HttpEntity<>(customerRequestModel);
            ResponseEntity<CustomerResponseModel> responseEntity = restTemplate.exchange(this.CUSTOMER_SERVICE_BASE_URL + "/" + customerId, HttpMethod.PUT, requestEntity, CustomerResponseModel.class);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeCustomer(String customerId) {

        try {
            this.restTemplate.delete(this.CUSTOMER_SERVICE_BASE_URL + "/" + customerId);

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

        return ex;
    }
}
