package com.example.apigateway.domainclientlayer;

import com.example.apigateway.presentationlayer.productdtos.StockItemRequestModel;
import com.example.apigateway.presentationlayer.productdtos.StockItemResponseModel;
import com.example.apigateway.utils.HttpErrorInfo;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
public class InventoryServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String INVENTORY_SERVICE_BASE_URL;

    public InventoryServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${app.products-service.host}") String inventoryServiceHost, @Value("${app.products-service.port}") String inventoryServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.INVENTORY_SERVICE_BASE_URL = "http://" + inventoryServiceHost + ":" + inventoryServicePort + "/api/v1/stock-items";
    }

    public List<StockItemResponseModel> getAllStockItems() {
        try {
            ResponseEntity<List<StockItemResponseModel>> stockItemResponseModels = this.restTemplate.exchange(this.INVENTORY_SERVICE_BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<StockItemResponseModel>>() {
            });

            return stockItemResponseModels.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public StockItemResponseModel getStockItemByProductId(String productId) {
        try {
            StockItemResponseModel stockItemResponseModel = this.restTemplate.getForObject(this.INVENTORY_SERVICE_BASE_URL + "/" + productId, StockItemResponseModel.class);
            return stockItemResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public StockItemResponseModel addStockItem(StockItemRequestModel stockItemRequestModel) {
        try {
            StockItemResponseModel stockItemResponseModel = this.restTemplate.postForObject(this.INVENTORY_SERVICE_BASE_URL, stockItemRequestModel, StockItemResponseModel.class);

            return stockItemResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public StockItemResponseModel updateStockItem(String productId, StockItemRequestModel stockItemRequestModel) {
        try {
            HttpEntity<StockItemRequestModel> requestEntity = new HttpEntity<>(stockItemRequestModel);
            ResponseEntity<StockItemResponseModel> responseEntity = restTemplate.exchange(this.INVENTORY_SERVICE_BASE_URL + "/" + productId, HttpMethod.PUT, requestEntity, StockItemResponseModel.class);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeStockItem(String productId) {
        try {
            this.restTemplate.delete(this.INVENTORY_SERVICE_BASE_URL + "/" + productId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public StockItemResponseModel reorderStock(String productId) {
        try {
            StockItemResponseModel stockItemResponseModel = this.restTemplate.postForObject(this.INVENTORY_SERVICE_BASE_URL + "/" + productId + "/reorder", null, StockItemResponseModel.class);

            return stockItemResponseModel;
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