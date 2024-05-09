package com.example.ordersservice.datamapperlayer;

import com.example.ordersservice.datalayer.Order;
import com.example.ordersservice.datalayer.OrderItem;
import com.example.ordersservice.datalayer.CustomerDetails;
import com.example.ordersservice.presentationlayer.CustomerDetailsResponseModel;
import com.example.ordersservice.presentationlayer.OrderItemResponseModel;
import com.example.ordersservice.presentationlayer.OrderResponseModel;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderResponseMapper {

    @Mapping(target = "orderIdentifier", source = "order.orderIdentifier.orderId")
    @Mapping(target = "status", source = "order.status")
    OrderResponseModel entityToResponseModel(Order order);

    @Mapping(target = "orderItemIdentifier", source = "orderItem.orderItemIdentifier.orderItemId")
    OrderItemResponseModel entityToResponseModel(OrderItem orderItem);

    CustomerDetailsResponseModel entityToResponseModel(CustomerDetails customerDetails);
}