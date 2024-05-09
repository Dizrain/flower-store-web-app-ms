package com.example.apigateway.domainclientlayer;

import com.example.apigateway.presentationlayer.orderdtos.OrderItemResponseModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderItemUpdateRequestModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderRequestModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderResponseModel;
import com.example.apigateway.presentationlayer.productdtos.CustomerIdentifier;
import com.example.apigateway.presentationlayer.productdtos.OrderStatus;
import com.example.apigateway.utils.HttpErrorInfo;
import com.example.apigateway.utils.exceptions.InvalidInputException;
import com.example.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class OrderServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String ORDER_SERVICE_BASE_URL;

    public OrderServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${app.orders-service.host}") String ordersServiceHost, @Value("${app.orders-service.port}") String ordersServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.ORDER_SERVICE_BASE_URL = "http://" + ordersServiceHost + ":" + ordersServicePort + "/api/v1/orders";
    }

    public List<OrderResponseModel> getAllOrders() {
        try {
            OrderResponseModel[] response = restTemplate.getForObject(ORDER_SERVICE_BASE_URL, OrderResponseModel[].class);
            return Arrays.asList(response);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public OrderResponseModel getOrderById(String orderId) {
        try {
            return restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/" + orderId, OrderResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel) {
        try {
            return restTemplate.postForObject(ORDER_SERVICE_BASE_URL, orderRequestModel, OrderResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public OrderResponseModel updateOrder(String orderId, OrderRequestModel orderRequestModel) {
        try {
            restTemplate.put(ORDER_SERVICE_BASE_URL + "/" + orderId, orderRequestModel);
            return getOrderById(orderId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeOrder(String orderId) {
        try {
            restTemplate.delete(ORDER_SERVICE_BASE_URL + "/" + orderId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public List<OrderItemResponseModel> getAllOrderItems(String orderIdentifier) {
        try {
            OrderItemResponseModel[] response = restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/" + orderIdentifier + "/items", OrderItemResponseModel[].class);
            return Arrays.asList(response);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public OrderItemResponseModel getOrderItem(String orderIdentifier, String orderItemId) {
        try {
            return restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/" + orderIdentifier + "/items/" + orderItemId, OrderItemResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public OrderItemResponseModel updateOrderItem(String orderIdentifier, String orderItemIdentifier, OrderItemUpdateRequestModel itemUpdate) {
        try {
            restTemplate.put(ORDER_SERVICE_BASE_URL + "/" + orderIdentifier + "/items/" + orderItemIdentifier, itemUpdate);
            return getOrderItem(orderIdentifier, itemUpdate.getItemId());
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public OrderResponseModel updateOrderStatus(String orderIdentifier, OrderStatus newStatus) {
        try {
            restTemplate.patchForObject(ORDER_SERVICE_BASE_URL + "/" + orderIdentifier + "/status?newStatus=" + newStatus, null, OrderResponseModel.class);
            return getOrderById(orderIdentifier);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public List<OrderResponseModel> getOrdersByCustomer(CustomerIdentifier customerId) {
       try {
            OrderResponseModel[] response = restTemplate.getForObject(ORDER_SERVICE_BASE_URL + "/customer/" + customerId, OrderResponseModel[].class);
            return Arrays.asList(response);
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