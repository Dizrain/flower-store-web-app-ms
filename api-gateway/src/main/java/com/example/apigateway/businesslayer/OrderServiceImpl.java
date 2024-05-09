package com.example.apigateway.businesslayer;

import com.example.apigateway.domainclientlayer.OrderServiceClient;
import com.example.apigateway.presentationlayer.orderdtos.OrderItemResponseModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderItemUpdateRequestModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderRequestModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderResponseModel;
import com.example.apigateway.presentationlayer.productdtos.CustomerIdentifier;
import com.example.apigateway.presentationlayer.productdtos.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderServiceClient orderServiceClient;


    @Override
    public List<OrderResponseModel> getAllOrders() {
        return  orderServiceClient.getAllOrders();
    }

    @Override
    public OrderResponseModel getOrderById(String orderId) {
        return orderServiceClient.getOrderById(orderId);
    }

    @Override
    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel) {
        return orderServiceClient.addOrder(orderRequestModel);
    }

    @Override
    public OrderResponseModel updateOrder(OrderRequestModel orderRequestModel, String orderId) {
        return orderServiceClient.updateOrder(orderId, orderRequestModel);
    }

    @Override
    public void removeOrder(String orderId) {
        orderServiceClient.removeOrder(orderId);
    }

    @Override
    public List<OrderItemResponseModel> getAllOrderItems(String orderIdentifier) {
        return orderServiceClient.getAllOrderItems(orderIdentifier);
    }

    @Override
    public OrderItemResponseModel getOrderItem(String orderIdentifier, String orderItemId) {
        return orderServiceClient.getOrderItem(orderIdentifier, orderItemId);
    }

    @Override
    public OrderItemResponseModel updateOrderItem(String orderIdentifier, String orderItemIdentifier, OrderItemUpdateRequestModel itemUpdate) {
        return orderServiceClient.updateOrderItem(orderIdentifier, orderItemIdentifier, itemUpdate);
    }

    @Override
    public OrderResponseModel updateOrderStatus(String orderIdentifier, OrderStatus newStatus) {
        return orderServiceClient.updateOrderStatus(orderIdentifier, newStatus);
    }

    @Override
    public List<OrderResponseModel> getOrdersByCustomer(CustomerIdentifier customerId) {
        return orderServiceClient.getOrdersByCustomer(customerId);
    }
}