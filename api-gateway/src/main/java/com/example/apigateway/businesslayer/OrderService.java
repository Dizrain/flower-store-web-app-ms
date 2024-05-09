package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.orderdtos.OrderItemResponseModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderItemUpdateRequestModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderRequestModel;
import com.example.apigateway.presentationlayer.orderdtos.OrderResponseModel;
import com.example.apigateway.presentationlayer.productdtos.CustomerIdentifier;
import com.example.apigateway.presentationlayer.productdtos.OrderStatus;

import java.util.List;

public interface OrderService {

    List<OrderResponseModel> getAllOrders();

    OrderResponseModel getOrderById(String orderId);

    OrderResponseModel addOrder(OrderRequestModel orderRequestModel);

    OrderResponseModel updateOrder(OrderRequestModel updatedOrder, String orderId);

    void removeOrder(String orderId);

    List<OrderItemResponseModel> getAllOrderItems(String orderIdentifier);

    OrderItemResponseModel getOrderItem(String orderIdentifier, String orderItemId);

    OrderItemResponseModel updateOrderItem(String orderIdentifier, String orderItemIdentifier, OrderItemUpdateRequestModel itemUpdate);

    OrderResponseModel updateOrderStatus(String orderIdentifier, OrderStatus newStatus);

    List<OrderResponseModel> getOrdersByCustomer(CustomerIdentifier customerId);
}