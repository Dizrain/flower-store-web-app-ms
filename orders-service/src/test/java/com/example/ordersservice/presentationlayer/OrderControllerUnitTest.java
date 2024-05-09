package com.example.ordersservice.presentationlayer;

import com.example.ordersservice.businesslayer.OrderService;
import com.example.ordersservice.datalayer.CustomerIdentifier;
import com.example.ordersservice.datalayer.OrderStatus;
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
    public void setUp() {
        // Initialize OrderItemRequestModel
        OrderItemRequestModel itemRequestModel = OrderItemRequestModel.builder()
                .productId("prod1")
                .quantity(2)
                .build();

        // Initialize OrderRequestModel
        orderRequestModel = OrderRequestModel.builder()
                .customerDetails(CustomerDetailsRequestModel.builder()
                        .customerId("customer1")
                        .name("Test Name")
                        .email("test@example.com")
                        .contactNumber("1234567890")
                        .address("Test Address")
                        .build())
                .items(Arrays.asList(itemRequestModel))
                .build();

        // Initialize OrderItemResponseModel
        OrderItemResponseModel itemResponseModel = OrderItemResponseModel.builder()
                .orderItemIdentifier("item1")
                .productId("prod1")
                .quantity(2)
                .price(50.0)
                .build();

        // Initialize OrderResponseModel
        orderResponseModel = OrderResponseModel.builder()
                .orderIdentifier("order1")
                .status(OrderStatus.PLACED.toString())
                .customerDetails(CustomerDetailsResponseModel.builder()
                        .customerId("customer1")
                        .name("Test Name")
                        .email("test@example.com")
                        .contactNumber("1234567890")
                        .address("Test Address")
                        .build())
                .items(Arrays.asList(itemResponseModel))
                .totalPrice(100.0)
                .build();
    }

    @Test
    public void whenCreateOrder_thenReturnCreatedOrder() {
        Mockito.when(orderService.createOrder(any(OrderRequestModel.class))).thenReturn(orderResponseModel);

        ResponseEntity<OrderResponseModel> response = orderController.createOrder(orderRequestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderResponseModel, response.getBody());
        verify(orderService, times(1)).createOrder(any(OrderRequestModel.class));
    }

    @Test
    public void whenGetOrder_thenReturnOrder() {
        Mockito.when(orderService.getOrder(anyString())).thenReturn(orderResponseModel);

        ResponseEntity<OrderResponseModel> response = orderController.getOrder("order1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderResponseModel, response.getBody());
        verify(orderService, times(1)).getOrder(anyString());
    }

    @Test
    public void whenDeleteOrder_thenNoContent() {
        Mockito.doNothing().when(orderService).deleteOrder(anyString());

        ResponseEntity<Void> response = orderController.deleteOrder("order1");

        assertEquals(204, response.getStatusCodeValue());
        verify(orderService, times(1)).deleteOrder(anyString());
    }

    @Test
    public void whenGetOrdersByCustomer_thenReturnOrders() {
        Mockito.when(orderService.getOrdersByCustomer(any(CustomerIdentifier.class))).thenReturn(Arrays.asList(orderResponseModel));

        ResponseEntity<List<OrderResponseModel>> response = orderController.getOrdersByCustomer(new CustomerIdentifier("customer1"));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(orderResponseModel, response.getBody().get(0));
        verify(orderService, times(1)).getOrdersByCustomer(any(CustomerIdentifier.class));
    }

    @Test
    public void whenUpdateOrderStatus_thenReturnUpdatedOrder() {
        Mockito.when(orderService.updateOrderStatus(anyString(), any(OrderStatus.class))).thenReturn(orderResponseModel);

        ResponseEntity<OrderResponseModel> response = orderController.updateOrderStatus("order1", OrderStatus.DELIVERED);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderResponseModel, response.getBody());
        verify(orderService, times(1)).updateOrderStatus(anyString(), any(OrderStatus.class));
    }

    @Test
    public void whenGetAllOrderItems_thenReturnOrderItems() {
        Mockito.when(orderService.getAllOrderItems(anyString())).thenReturn(Arrays.asList(new OrderItemResponseModel()));

        ResponseEntity<List<OrderItemResponseModel>> response = orderController.getAllOrderItems("order1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(orderService, times(1)).getAllOrderItems(anyString());
    }

    @Test
    public void whenGetOrderItem_thenReturnOrderItem() {
        Mockito.when(orderService.getOrderItem(anyString(), anyString())).thenReturn(new OrderItemResponseModel());

        ResponseEntity<OrderItemResponseModel> response = orderController.getOrderItem("order1", "item1");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(orderService, times(1)).getOrderItem(anyString(), anyString());
    }

    @Test
    public void whenUpdateOrderItem_thenReturnUpdatedOrderItem() {
        OrderItemUpdateRequestModel itemUpdate = new OrderItemUpdateRequestModel("item1", "prod1", 2);
        Mockito.when(orderService.updateOrderItem(anyString(), any(OrderItemUpdateRequestModel.class))).thenReturn(new OrderItemResponseModel());
        ResponseEntity<OrderItemResponseModel> response = orderController.updateOrderItem("order1", "item1", itemUpdate);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(orderService, times(1)).updateOrderItem(anyString(), any(OrderItemUpdateRequestModel.class));
    }
}