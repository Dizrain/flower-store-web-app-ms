package com.example.ordersservice.businesslayer;

import com.example.ordersservice.datalayer.CustomerIdentifier;
import com.example.ordersservice.datalayer.OrderStatus;
import com.example.ordersservice.presentationlayer.OrderItemResponseModel;
import com.example.ordersservice.presentationlayer.OrderItemUpdateRequestModel;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import com.example.ordersservice.presentationlayer.OrderResponseModel;

import java.util.List;

public interface OrderService {
    OrderResponseModel getOrder(String orderIdentifier);
    OrderResponseModel createOrder(OrderRequestModel requestModel);
    OrderItemResponseModel updateOrderItem(String orderIdentifier, OrderItemUpdateRequestModel itemUpdate);
    List<OrderItemResponseModel> getAllOrderItems(String orderIdentifier);
    OrderItemResponseModel getOrderItem(String orderIdentifier, String orderItemIdentifier);
    void deleteOrder(String orderIdentifier);
    List<OrderResponseModel> getOrdersByCustomer(CustomerIdentifier customerIdentifier);
    OrderResponseModel updateOrderStatus(String orderIdentifier, OrderStatus newStatus);
}