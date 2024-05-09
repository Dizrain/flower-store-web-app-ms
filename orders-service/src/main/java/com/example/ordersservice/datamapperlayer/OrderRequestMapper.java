package com.example.ordersservice.datamapperlayer;

import com.example.ordersservice.datalayer.*;
import com.example.ordersservice.presentationlayer.CustomerDetailsRequestModel;
import com.example.ordersservice.presentationlayer.OrderItemRequestModel;
import com.example.ordersservice.presentationlayer.OrderItemUpdateRequestModel;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target="id", ignore = true)
    @Mapping(target="totalPrice", ignore = true)
    @Mapping(target="items", ignore = true)
    Order requestModelToEntity(OrderRequestModel orderRequestModel, OrderIdentifier orderIdentifier);

    @Mapping(target="id", ignore = true)
    OrderItem mapItem(OrderItemRequestModel orderItemRequestModel, OrderItemIdentifier orderItemIdentifier);

    CustomerDetails requestModelToEntity(CustomerDetailsRequestModel customerDetailsRequestModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItemIdentifier", ignore = true)
    @Mapping(target="price", ignore = true)
    void updateEntity(OrderItemUpdateRequestModel orderItemUpdateRequestModel, @MappingTarget OrderItem orderItem);
}