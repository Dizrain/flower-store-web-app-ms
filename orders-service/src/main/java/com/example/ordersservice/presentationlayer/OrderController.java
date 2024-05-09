package com.example.ordersservice.presentationlayer;

import com.example.ordersservice.datalayer.CustomerIdentifier;
import com.example.ordersservice.datalayer.OrderStatus;
import com.example.ordersservice.businesslayer.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseModel> createOrder(@RequestBody OrderRequestModel requestModel) {
        OrderResponseModel responseModel = orderService.createOrder(requestModel);
        return ResponseEntity.ok(responseModel);
    }

    @GetMapping("/{orderIdentifier}/items")
    public ResponseEntity<List<OrderItemResponseModel>> getAllOrderItems(@PathVariable String orderIdentifier) {
        List<OrderItemResponseModel> responseModels = orderService.getAllOrderItems(orderIdentifier);
        return ResponseEntity.ok(responseModels);
    }

    @GetMapping("/{orderIdentifier}/items/{orderItemId}")
    public ResponseEntity<OrderItemResponseModel> getOrderItem(@PathVariable String orderIdentifier, @PathVariable String orderItemId) {
        OrderItemResponseModel responseModel = orderService.getOrderItem(orderIdentifier, orderItemId);
        return ResponseEntity.ok(responseModel);
    }

    @PutMapping("/{orderIdentifier}/items/{orderItemIdentifier}")
    public ResponseEntity<OrderItemResponseModel> updateOrderItem(@PathVariable String orderIdentifier,
                                                                  @PathVariable String orderItemIdentifier,
                                                                  @RequestBody OrderItemUpdateRequestModel itemUpdate) {
        OrderItemResponseModel responseModel = orderService.updateOrderItem(orderIdentifier, orderItemIdentifier, itemUpdate);
        return ResponseEntity.ok(responseModel);
    }

    @PatchMapping("/{orderIdentifier}/status")
    public ResponseEntity<OrderResponseModel> updateOrderStatus(@PathVariable String orderIdentifier,
                                                                @RequestParam OrderStatus newStatus) {
        OrderResponseModel responseModel = orderService.updateOrderStatus(orderIdentifier, newStatus);
        return ResponseEntity.ok(responseModel);
    }

    @GetMapping("/{orderIdentifier}")
    public ResponseEntity<OrderResponseModel> getOrder(@PathVariable String orderIdentifier) {
        OrderResponseModel responseModel = orderService.getOrder(orderIdentifier);
        return ResponseEntity.ok(responseModel);
    }

    @DeleteMapping("/{orderIdentifier}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderIdentifier) {
        orderService.deleteOrder(orderIdentifier);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseModel>> getOrdersByCustomer(@PathVariable CustomerIdentifier customerId) {
        List<OrderResponseModel> responseModels = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(responseModels);
    }
}