package com.example.ordersservice.presentationlayer;

import com.example.ordersservice.datalayer.*;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentRequestModel;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentResponseModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemRequestModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemResponseModel;
import com.example.ordersservice.datamapperlayer.productdtos.ProductResponseModel;
import com.example.ordersservice.domainclientlayer.PaymentsServiceClient;
import com.example.ordersservice.domainclientlayer.ProductsServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private PaymentsServiceClient paymentsServiceClient;

    @MockBean
    private ProductsServiceClient productsServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(OrderControllerIntegrationTest.class);

    private final String BASE_URL = "/api/v1/orders";

    @BeforeEach
    public void setUp() {
        logger.info("Setting up the test data...");

        orderRepository.deleteAll();

        // Create and save an order associated with the customer
        Order order = new Order();
        Order.OrderItem orderItem = new Order.OrderItem();
        orderItem.setProductId(new ProductIdentifier("sample-product-id"));
        order.setPaymentId(new PaymentIdentifier("sample-payment-id"));
        orderItem.setQuantity(2);
        order.setOrderIdentifier(new OrderIdentifier());
        order.setCustomerId(new CustomerIdentifier());
        order.setShippingAddress("Sample Address");
        order.setBillingInformation("Sample Billing Information");
        order.setItems(Arrays.asList(
                orderItem
        ));
        order.setStatus(OrderStatus.PLACED);
        orderRepository.save(order);

        // Mock the calls
        PaymentResponseModel paymentResponseModel = new PaymentResponseModel(
                "test-payment-id",
                100.0,
                LocalDateTime.now(),
                PaymentMethod.CreditCard
        );
        when(paymentsServiceClient.processPayment(any(PaymentRequestModel.class))).thenReturn(paymentResponseModel);
        StockItemResponseModel stockItemResponseModel = new StockItemResponseModel("test-product-id", "test-product-id", 10, 10);
        when(productsServiceClient.getStockItemByProductId(any())).thenReturn(stockItemResponseModel);
        when(productsServiceClient.updateStockItem(any(StockItemRequestModel.class))).thenReturn(stockItemResponseModel);
        ProductResponseModel mockProduct = ProductResponseModel.builder().
                productId("sample-product-id").
                name("Sample Product").
                description("Sample Description").
                categoryIds(Arrays.asList("sample-category-id")).
                price(100.0).
                color("Sample Color").
                type("Sample Type").
                inSeason(true).
                build();
        when(productsServiceClient.getProductByProductId(anyString())).thenReturn(mockProduct);

        logger.info("Test data has been set up.");
    }

    @Test
    public void whenGetAllOrders_thenReturnAllOrders() {
        logger.info("Getting all orders...");

        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderResponseModel.class)
                .hasSize(1);
    }

    @Test
    public void whenGetOrderById_thenReturnOrder() {
        // TODO: Add test logic here, similar to the test logic in ProductControllerIntegrationTest
    }

    @Test
    public void whenGetOrderById_thenOrderNotFound() {
        // TODO: Add test logic here, similar to the test logic in ProductControllerIntegrationTest
    }

    @Test
    public void whenCreateOrder_thenReturnCreatedOrder() {
        // Create an OrderRequestModel object
        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .customerId("test-customer-id")
                .items(Arrays.asList(OrderRequestModel.OrderItemModel.builder()
                        .productId("test-product-id")
                        .quantity(1)
                        .build()))
                .shippingAddress("Test Address")
                .billingInformation("Test Billing Information")
                .build();

        // Send a POST request to the /api/v1/orders endpoint with the OrderRequestModel object
        webTestClient.post().uri("/api/v1/orders")
                .bodyValue(orderRequestModel)
                .exchange()
                // Verify that the response status is 201 Created
                .expectStatus().isCreated()
                // Verify that the response body contains the created order
                .expectBody(OrderResponseModel.class)
                .value(response -> {
                    response.getCustomerId().equals(orderRequestModel.getCustomerId());
                    response.getItems().get(0).getProductId().equals(orderRequestModel.getItems().get(0).getProductId());
                    response.getShippingAddress().equals(orderRequestModel.getShippingAddress());
                });
    }

    @Test
    public void whenUpdateOrder_thenReturnUpdatedOrder() {
        // Get existing product
        Order originalOrder =  orderRepository.findAll().get(0);

        // Modify the original order to create an updated order
        OrderRequestModel updatedOrder = OrderRequestModel.builder()
                .customerId(originalOrder.getCustomerId().getCustomerId())
                // Set items to the original order items
                .items(originalOrder.getItems().stream()
                        .map(item -> OrderRequestModel.OrderItemModel.builder()
                                .productId(item.getProductId().getProductId())
                                .quantity(item.getQuantity())
                                .build())
                        .toList())
                .shippingAddress("Updated Address")
                .billingInformation("Updated Billing Information")
                .build();


        // Send a PUT request to update the order
        webTestClient.put().uri("/api/v1/orders/" + originalOrder.getOrderIdentifier().getOrderId())
                .bodyValue(updatedOrder)
                .exchange()
                // Verify that the response status is 200 OK
                .expectStatus().isOk()
                // Verify that the response body contains the updated order
                .expectBody(OrderResponseModel.class)
                .value(response -> {
                    response.getCustomerId().equals(updatedOrder.getCustomerId());
                    response.getItems().get(0).getProductId().equals(updatedOrder.getItems().get(0).getProductId());
                    response.getShippingAddress().equals(updatedOrder.getShippingAddress());
                });
    }

    @Test
    public void whenDeleteOrder_thenOrderIsDeleted() {
        // TODO: Add test logic here, similar to the test logic in ProductControllerIntegrationTest
    }
}