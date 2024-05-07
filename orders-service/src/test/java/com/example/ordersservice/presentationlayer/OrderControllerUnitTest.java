package com.example.ordersservice.presentationlayer;

import com.example.ordersservice.businesslayer.OrderService;
import com.example.ordersservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = OrderController.class)
public class OrderControllerUnitTest {

    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderService orderService;

    private OrderRequestModel orderRequestModel;
    private OrderResponseModel orderResponseModel;

    @BeforeEach
    public void setup() {
        // Initialize orderRequestModel
        OrderRequestModel.OrderItemModel itemModel = OrderRequestModel.OrderItemModel.builder()
                .productId("prod1")
                .quantity(2)
                .build();

        orderRequestModel = OrderRequestModel.builder()
                .customerId("cust1")
                .items(Arrays.asList(itemModel))
                .shippingAddress("123 Test St")
                .billingInformation("456 Test Ave")
                .build();

        // Initialize orderResponseModel
        OrderResponseModel.OrderItemModel responseItemModel = OrderResponseModel.OrderItemModel.builder()
                .productId("prod1")
                .quantity(2)
                .build();

        orderResponseModel = OrderResponseModel.builder()
                .orderId("order1")
                .customerId("cust1")
                .items(Arrays.asList(responseItemModel))
                .shippingAddress("123 Test St")
                .billingInformation("456 Test Ave")
                .status("PLACED")
                .build();
    }

    @Test
    public void getAllOrders_thenReturnAllOrders() {
        Mockito.when(orderService.getAllOrders()).thenReturn(Arrays.asList(orderResponseModel));

        ResponseEntity<List<OrderResponseModel>> response = orderController.getAllOrders();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(orderResponseModel, response.getBody().get(0));
    }

    @Test
    public void getOrderById_thenReturnOrder() {
        Mockito.when(orderService.getOrderById(anyString())).thenReturn(orderResponseModel);

        ResponseEntity<OrderResponseModel> response = orderController.getOrderById("order1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderResponseModel, response.getBody());
    }

    @Test
    public void whenOrderNotFoundOnGet_thenThrowNotFoundException(){
        Mockito.when(orderService.getOrderById(anyString())).thenThrow(new NotFoundException("Order not found with id order1"));

        String NOT_FOUND_ORDER_ID = "order1";
        assertThrowsExactly(NotFoundException.class, ()->
                orderController.getOrderById(NOT_FOUND_ORDER_ID));

        verify(orderService, times(1)).getOrderById(NOT_FOUND_ORDER_ID);
    }

    @Test
    public void createOrder_thenReturnCreatedOrder() {
        Mockito.when(orderService.createOrder(any(OrderRequestModel.class))).thenReturn(orderResponseModel);

        ResponseEntity<OrderResponseModel> response = orderController.createOrder(orderRequestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(orderResponseModel, response.getBody());
    }

    @Test
    public void updateOrder_thenReturnUpdatedOrder() {
        Mockito.when(orderService.updateOrder(any(OrderRequestModel.class), anyString())).thenReturn(orderResponseModel);

        ResponseEntity<OrderResponseModel> response = orderController.updateOrder("order1", orderRequestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderResponseModel, response.getBody());
    }

    @Test
    public void directMockTest() {
        Mockito.doThrow(new NotFoundException("Order not found with id order1"))
                .when(orderService).updateOrder(any(OrderRequestModel.class), eq("order1"));

        // Directly test the mocked service
        assertThrows(NotFoundException.class, () -> orderService.updateOrder(orderRequestModel, "order1"));
    }

    @Test
    public void whenOrderNotFoundOnUpdate_thenThrowNotFoundException() {
        Mockito.doThrow(new NotFoundException("Order not found with id order1")).when(orderService).updateOrder(any(OrderRequestModel.class), anyString());

        assertThrows(NotFoundException.class, () -> orderController.updateOrder("order1", orderRequestModel));
    }

    @Test
    public void cancelOrder_thenStatusNoContent() {
        Mockito.doNothing().when(orderService).cancelOrder(anyString());

        ResponseEntity<Void> response = orderController.cancelOrder("order1");

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void whenOrderNotFoundOnCancel_thenThrowNotFoundException() {
        Mockito.doThrow(new NotFoundException("Order not found with id order1")).when(orderService).cancelOrder(anyString());

        assertThrows(NotFoundException.class, () -> orderController.cancelOrder("order1"));
    }
}