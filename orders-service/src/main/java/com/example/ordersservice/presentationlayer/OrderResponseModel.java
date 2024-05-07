package com.example.ordersservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseModel extends RepresentationModel<OrderResponseModel> {

    String orderId;
    String customerId;
    String paymentId;
    List<OrderItemModel> items;
    String shippingAddress;
    String billingInformation;
    String status;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemModel extends RepresentationModel<OrderResponseModel> {
        String productId;
        int quantity;
    }
}
