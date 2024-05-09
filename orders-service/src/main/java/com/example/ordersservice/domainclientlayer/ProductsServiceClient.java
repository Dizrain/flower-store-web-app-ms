package com.example.ordersservice.domainclientlayer;


import com.example.ordersservice.datamapperlayer.productdtos.StockItemRequestModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemResponseModel;
import com.example.ordersservice.utils.exceptions.InvalidInputException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.example.ordersservice.utils.HttpErrorInfo;
import com.example.ordersservice.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import com.example.ordersservice.datamapperlayer.productdtos.ProductResponseModel;


import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class ProductsServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String STOCK_ITEM_SERVICE_BASE_URL;

    public ProductsServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                 @Value("${app.products-service.host}") String productServiceHost,
                                 @Value("${app.products-service.port}") String productServicePort) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        STOCK_ITEM_SERVICE_BASE_URL = "http://" + productServiceHost + ":" +
                productServicePort + "/api/v1/stock-items";
    }

    public StockItemResponseModel getStockItemByProductId(String productId) {
        try {
            String url = STOCK_ITEM_SERVICE_BASE_URL + "/" + productId;

            StockItemResponseModel stockItemResponseModel = restTemplate.getForObject(url,
                    StockItemResponseModel.class);

            return stockItemResponseModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public StockItemResponseModel updateStockItem(StockItemRequestModel stockItemRequestModel) {
        try {
            String url = STOCK_ITEM_SERVICE_BASE_URL + "/" + stockItemRequestModel.getProductId();

            HttpEntity<StockItemRequestModel> requestEntity = new HttpEntity<>(stockItemRequestModel);
            ResponseEntity<StockItemResponseModel> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, StockItemResponseModel.class);

            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public boolean isProductAvailable(String productId, int quantity) {
        StockItemResponseModel stockItem = getStockItemByProductId(productId);
        log.info("Stock level for product {} is {}", productId, stockItem.getStockLevel());
        return stockItem.getStockLevel() >= quantity;
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

        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY){
            return new InvalidInputException(getErrorMessage(ex));
        }

        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }

}

