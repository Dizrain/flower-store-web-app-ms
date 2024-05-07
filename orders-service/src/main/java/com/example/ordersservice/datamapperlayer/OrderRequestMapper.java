package com.example.ordersservice.datamapperlayer;

import com.example.ordersservice.datalayer.Order;
import com.example.ordersservice.datalayer.OrderIdentifier;
import com.example.ordersservice.datalayer.ProductIdentifier;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    @Mapping(target = "id", ignore = true) // Assume auto-generation or setting elsewhere
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    Order requestModelToEntity(OrderRequestModel model, OrderIdentifier orderIdentifier);

    @Mapping(target = "id", ignore = true) // Assume auto-generation or setting elsewhere
    @Mapping(target = "productId", ignore = true)
    Order.OrderItem orderItemModelToOrderItem(OrderRequestModel.OrderItemModel orderItemModel);
    // Map the product identifier from the OrderItemModel to the OrderItem entity
    default ProductIdentifier productIdentifierFromOrderItemModel(OrderRequestModel.OrderItemModel orderItemModel) {
        return new ProductIdentifier(orderItemModel.getProductId());
    }
}