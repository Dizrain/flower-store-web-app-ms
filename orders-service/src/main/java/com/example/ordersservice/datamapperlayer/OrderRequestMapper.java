package com.example.flowerstorewebapp.orderprocessingsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.orderprocessingsubdomain.datalayer.Order;
import com.example.flowerstorewebapp.orderprocessingsubdomain.datalayer.OrderIdentifier;
import com.example.flowerstorewebapp.orderprocessingsubdomain.presentationlayer.OrderRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    @Mapping(target = "id", ignore = true) // Assume auto-generation or setting elsewhere
    @Mapping(target = "status", ignore = true) // Assume default status or setting elsewhere
    Order requestModelToEntity(OrderRequestModel model, OrderIdentifier orderIdentifier);

    @Mapping(target = "id", ignore = true) // Assume auto-generation or setting elsewhere
    Order.OrderItem orderItemModelToOrderItem(OrderRequestModel.OrderItemModel orderItemModel);
}