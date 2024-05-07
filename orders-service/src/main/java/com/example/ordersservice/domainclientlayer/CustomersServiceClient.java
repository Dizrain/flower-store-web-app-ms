package com.example.ordersservice.domainclientlayer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.example.ordersservice.datamapperlayer.customerdtos.CustomerResponseModel;
import com.example.ordersservice.utils.HttpErrorInfo;
import com.example.ordersservice.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class CustomersServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String CLIENT_SERVICE_BASE_URL;

    public CustomersServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                  @Value("${app.customers-service.host}") String clientServiceHost,
                                  @Value("${app.customers-service.port}") String clientServicePort) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        CLIENT_SERVICE_BASE_URL = "http://" + clientServiceHost + ":" +
                clientServicePort + "/api/v1/clients";
    }

    public CustomerResponseModel getCustomerByCustomerId(String clientId) {
        try {
            String url = CLIENT_SERVICE_BASE_URL + "/" + clientId;

            CustomerResponseModel clientResponseModel = restTemplate.getForObject(url,
                    CustomerResponseModel.class);

            return clientResponseModel;
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

        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }

        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

}

