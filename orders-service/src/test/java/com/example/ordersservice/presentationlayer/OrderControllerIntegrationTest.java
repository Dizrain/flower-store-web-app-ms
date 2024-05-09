package com.example.ordersservice.presentationlayer;

import com.example.ordersservice.datalayer.*;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentRequestModel;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentResponseModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemResponseModel;
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
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

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

    private OrderRequestModel orderRequestModel;
    private OrderRequestModel invalidOrderRequestModel;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();

        // Initialize OrderItemRequestModel
        OrderItemRequestModel itemRequestModel = OrderItemRequestModel.builder()
                .productId("Product 1")
                .quantity(2)
                .price(50.0)
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

        // Initialize invalid OrderRequestModel
        invalidOrderRequestModel = OrderRequestModel.builder()
                .customerDetails(CustomerDetailsRequestModel.builder()
                        .customerId("customer1")
                        .name("a") // Name too short
                        .email("") // Invalid email
                        .contactNumber("1234567890")
                        .address("Test Address")
                        .build())
                .items(Arrays.asList(itemRequestModel))
                .build();


        // Initialize OrderItemResponseModel
        OrderItemResponseModel itemResponseModel = OrderItemResponseModel.builder()
                .productId("Product 1")
                .quantity(2)
                .price(50.0)
                .build();

        // Create and save an order
        Order order = new Order();
        order.setOrderIdentifier(new OrderIdentifier("order1"));
        order.setStatus(OrderStatus.PLACED);
        order.setCustomerDetails(new CustomerDetails("customer1", "Test Name", "test@example.com", "1234567890", "Test Address"));
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemIdentifier(new OrderItemIdentifier("item1"));
        orderItem.setProductId("Product 1");
        orderItem.setQuantity(2);
        orderItem.setPrice(50.0);
        order.setItems(new HashSet<>(Arrays.asList(orderItem)));
        orderRepository.save(order);

        // Mocking the ProductsServiceClient
        when(productsServiceClient.getStockItemByProductId(anyString()))
                .thenReturn(new StockItemResponseModel("prod1", "Product 1", 10, 5));
        when(productsServiceClient.isProductAvailable(anyString(), anyInt())).thenReturn(true);
        // Mocking the PaymentsServiceClient
        when(paymentsServiceClient.processPayment(any(PaymentRequestModel.class)))
                .thenReturn(new PaymentResponseModel("payment1", 100.0, LocalDateTime.now(), PaymentMethod.CreditCard));
    }

    @Test
    public void whenCreateOrder_thenReturnCreatedOrder() {
        when(paymentsServiceClient.processPayment(any(PaymentRequestModel.class)))
                .thenReturn(new PaymentResponseModel("payment1", 100.0, LocalDateTime.now(), PaymentMethod.CreditCard));

        webTestClient.post().uri(BASE_URL)
                .bodyValue(orderRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponseModel.class)
                .value(orderResponse -> {
                    assert orderResponse.getStatus().equals(OrderStatus.PLACED.toString());
                    assert orderResponse.getCustomerDetails().getCustomerId().equals("customer1");
                    assert orderResponse.getCustomerDetails().getName().equals("Test Name");
                    assert orderResponse.getCustomerDetails().getEmail().equals("test@example.com");
                });
    }

    @Test
    public void whenCreateOrderWithOutOfStockProduct_thenThrowOutOfStockException() {
        when(productsServiceClient.isProductAvailable(anyString(), anyInt())).thenReturn(false);

        webTestClient.post().uri(BASE_URL)
                .bodyValue(orderRequestModel)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void whenCreateOrderWithInvalidData_thenThrowConstraintViolationException() {
        webTestClient.post().uri(BASE_URL)
                .bodyValue(invalidOrderRequestModel)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    public void whenGetOrder_thenReturnOrder() {
        webTestClient.get().uri(BASE_URL + "/order1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponseModel.class)
                .value(orderResponse -> {
                    assert orderResponse.getStatus().equals(OrderStatus.PLACED.toString());
                    assert orderResponse.getCustomerDetails().getCustomerId().equals("customer1");
                    assert orderResponse.getCustomerDetails().getName().equals("Test Name");
                    assert orderResponse.getCustomerDetails().getEmail().equals("test@example.com");
                });
    }

    @Test
    public void whenGetNonExistentOrder_thenThrowNotFoundException() {
        webTestClient.get().uri(BASE_URL + "/nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteOrder_thenNoContent() {
        webTestClient.delete().uri(BASE_URL + "/order1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenGetOrdersByCustomer_thenReturnOrders() {
        webTestClient.get().uri(BASE_URL + "/customer/customer1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderResponseModel.class)
                .hasSize(1);
    }

    @Test
    public void whenUpdateOrderStatus_thenReturnUpdatedOrder() {
        webTestClient.patch().uri(BASE_URL + "/order1/status?newStatus=DELIVERED")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponseModel.class)
                .value(orderResponse -> {
                    assert orderResponse.getStatus().equals(OrderStatus.DELIVERED.toString());
                    assert orderResponse.getCustomerDetails().getCustomerId().equals("customer1");
                    assert orderResponse.getCustomerDetails().getName().equals("Test Name");
                    assert orderResponse.getCustomerDetails().getEmail().equals("test@example.com");
                });
    }

    @Test
    public void whenUpdateOrderStatusToInvalid_thenThrowIllegalStateException() {
        // First update status from PLACED to DELIVERED
        webTestClient.patch().uri(BASE_URL + "/order1/status?newStatus=DELIVERED")
                .exchange()
                .expectStatus().isOk();

        // Then try to update status from DELIVERED to PLACED
        webTestClient.patch().uri(BASE_URL + "/order1/status?newStatus=PLACED")
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    public void whenGetAllOrderItems_thenReturnOrderItems() {
        webTestClient.get().uri(BASE_URL + "/order1/items")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderItemResponseModel.class)
                .hasSize(1);
    }

    @Test
    public void whenGetOrderItem_thenReturnOrderItem() {
        webTestClient.get().uri(BASE_URL + "/order1/items/item1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderItemResponseModel.class)
                .value(orderItemResponse -> {
                    assert orderItemResponse.getOrderItemId().equals("item1");
                    assert orderItemResponse.getProductId().equals("Product 1");
                    assert orderItemResponse.getQuantity() == 2;
                    assert orderItemResponse.getPrice() == 50.0;
                });
    }

    @Test
    public void whenGetNonExistentOrderItem_thenThrowNotFoundException() {
        webTestClient.get().uri(BASE_URL + "/order1/items/nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenUpdateOrderItem_thenReturnUpdatedOrderItem() {
        OrderItemUpdateRequestModel itemUpdate = new OrderItemUpdateRequestModel("prod1", 2);

        webTestClient.put().uri(BASE_URL + "/order1/items/item1")
                .bodyValue(itemUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderItemResponseModel.class)
                .value(orderItemResponse -> {
                    assert orderItemResponse.getOrderItemId().equals("item1");
                    assert orderItemResponse.getProductId().equals("prod1");
                    assert orderItemResponse.getQuantity() == 2;
                    assert orderItemResponse.getPrice() == 50.0;
                });
    }
}