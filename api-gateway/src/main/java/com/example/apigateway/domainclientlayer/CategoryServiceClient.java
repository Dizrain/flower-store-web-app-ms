package com.example.apigateway.domainclientlayer;

import com.example.apigateway.presentationlayer.productdtos.CategoryRequestModel;
import com.example.apigateway.presentationlayer.productdtos.CategoryResponseModel;
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
public class CategoryServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String CATEGORY_SERVICE_BASE_URL;

    public CategoryServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${app.products-service.host}") String categoriesServiceHost, @Value("${app.products-service.port}") String categoriesServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.CATEGORY_SERVICE_BASE_URL = "http://" + categoriesServiceHost + ":" + categoriesServicePort + "/api/v1/categories";
    }

    public List<CategoryResponseModel> getAllCategories() {
        try {
            ResponseEntity<List<CategoryResponseModel>> categoryResponseModels = this.restTemplate.exchange(this.CATEGORY_SERVICE_BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<CategoryResponseModel>>() {
            });

            return categoryResponseModels.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public CategoryResponseModel getCategoryById(String categoryId) {
        try {
            CategoryResponseModel categoryResponseModel = this.restTemplate.getForObject(this.CATEGORY_SERVICE_BASE_URL + "/" + categoryId, CategoryResponseModel.class);
            return categoryResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public CategoryResponseModel addCategory(CategoryRequestModel categoryRequestModel) {
        try {
            CategoryResponseModel categoryResponseModel = this.restTemplate.postForObject(this.CATEGORY_SERVICE_BASE_URL, categoryRequestModel, CategoryResponseModel.class);

            return categoryResponseModel;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public CategoryResponseModel updateCategory(String categoryId, CategoryRequestModel categoryRequestModel) {
        try {
            HttpEntity<CategoryRequestModel> requestEntity = new HttpEntity<>(categoryRequestModel);
            ResponseEntity<CategoryResponseModel> responseEntity = restTemplate.exchange(this.CATEGORY_SERVICE_BASE_URL + "/" + categoryId, HttpMethod.PUT, requestEntity, CategoryResponseModel.class);

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeCategory(String categoryId) {
        try {
            this.restTemplate.delete(this.CATEGORY_SERVICE_BASE_URL + "/" + categoryId);

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