package com.example.ordersservice.datamapperlayer;

import com.example.ordersservice.datalayer.Order;
import com.example.ordersservice.presentationlayer.OrderController;
import com.example.ordersservice.presentationlayer.OrderResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderResponseMapper {

    @Mapping(expression = "java(order.getOrderIdentifier().getOrderId())", target = "orderId")
    @Mapping(expression = "java(order.getPaymentId().getPaymentId())", target = "paymentId")
    @Mapping(target = "customerId", expression = "java(order.getCustomerId().getCustomerId())")
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "items", source = "order.items")
    OrderResponseModel entityToResponseModel(Order order);

    List<OrderResponseModel> entityListToResponseModelList(List<Order> orders);

    @AfterMapping
    default void addLinks(@MappingTarget OrderResponseModel model, Order order) {
        Link selfLink = linkTo(methodOn(OrderController.class).getOrderById(model.getOrderId())).withSelfRel();
        model.add(selfLink);

        // Example to add more links, adjust according to your API design
        Link cancelLink = linkTo(methodOn(OrderController.class).cancelOrder(model.getOrderId())).withRel("cancelOrder");
        model.add(cancelLink);
    }

    @Mapping(target = "productId", expression = "java(orderItem.getProductId().getProductId())")
    OrderResponseModel.OrderItemModel orderItemToOrderItemResponseModel(Order.OrderItem orderItem);
}