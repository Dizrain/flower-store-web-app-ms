package com.example.apigateway.domainclientlayer;

import com.example.apigateway.presentationlayer.productdtos.ProductRequestModel;
import com.example.apigateway.presentationlayer.productdtos.ProductResponseModel;
import com.example.apigateway.utils.HttpErrorInfo;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
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
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String PRODUCT_SERVICE_BASE_URL;

    public ProductServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${app.products-service.host}") String productsServiceHost, @Value("${app.products-service.port}") String productsServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.PRODUCT_SERVICE_BASE_URL = "http://" + productsServiceHost + ":" + productsServicePort + "/api/v1/products";
    }

    public List<ProductResponseModel> getAllProducts() {
        try {
            ResponseEntity<List<ProductResponseModel>> productResponseModels = this.restTemplate.exchange(this.PRODUCT_SERVICE_BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductResponseModel>>() {});

            return productResponseModels.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public ProductResponseModel getProductById(String productId) {
        try {
            ProductResponseModel productResponseModel = this.restTemplate.getForObject(this.PRODUCT_SERVICE_BASE_URL + "/" + productId, ProductResponseModel.class);
            return productResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public ProductResponseModel addProduct(ProductRequestModel productRequestModel) {
        try {
            ProductResponseModel productResponseModel = this.restTemplate.postForObject(this.PRODUCT_SERVICE_BASE_URL, productRequestModel, ProductResponseModel.class);

            return productResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public ProductResponseModel updateProduct(String productId, ProductRequestModel productRequestModel) {
        try {
            HttpEntity<ProductRequestModel> requestEntity = new HttpEntity<>(productRequestModel);
            ResponseEntity<ProductResponseModel> responseEntity = restTemplate.exchange(this.PRODUCT_SERVICE_BASE_URL + "/" + productId, HttpMethod.PUT, requestEntity, ProductResponseModel.class);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeProduct(String productId) {
        try {
            this.restTemplate.delete(this.PRODUCT_SERVICE_BASE_URL + "/" + productId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public List<ProductResponseModel> getProductsByCategory(Long categoryId) {
        try {
            ResponseEntity<List<ProductResponseModel>> productResponseModels = this.restTemplate.exchange(this.PRODUCT_SERVICE_BASE_URL + "/category/" + categoryId, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductResponseModel>>() {});

            return productResponseModels.getBody();
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