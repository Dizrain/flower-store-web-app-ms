package com.example.flowerstorewebapp.orderprocessingsubdomain.presentationlayer;

import com.example.flowerstorewebapp.orderprocessingsubdomain.businesslayer.OrderService;
import com.example.flowerstorewebapp.orderprocessingsubdomain.presentationlayer.OrderRequestModel;
import com.example.flowerstorewebapp.orderprocessingsubdomain.presentationlayer.OrderResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders") // Define the base URL for order-related requests
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponseModel>> getAllOrders() {
        List<OrderResponseModel> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseModel> getOrderById(@PathVariable String orderId) {
        OrderResponseModel order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping()
    public ResponseEntity<OrderResponseModel> createOrder(@RequestBody OrderRequestModel orderRequestModel) {
        OrderResponseModel savedOrder = orderService.createOrder(orderRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseModel> updateOrder(@PathVariable String orderId, @RequestBody OrderRequestModel orderRequestModel) {
        OrderResponseModel updatedOrder = orderService.updateOrder(orderRequestModel, orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // If you have additional endpoints, such as filtering orders by customer ID, you can add them here.
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseModel>> getOrdersByCustomerId(@PathVariable String customerId) {
        List<OrderResponseModel> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}
