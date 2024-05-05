package com.example.paymentservice.presentationlayer;

import com.example.paymentservice.datalayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentRepository paymentRepository;

    private final String BASE_URL = "/api/v1/payments";

    @BeforeEach
    public void setUp() {
        paymentRepository.deleteAll();

        // Create and save a payment
        Payment payment = new Payment(100.0, LocalDateTime.now(), PaymentMethod.CreditCard);
        paymentRepository.save(payment);

        // Create and save another payment
        Payment anotherPayment = new Payment(200.0, LocalDateTime.now(), PaymentMethod.PayPal);
        paymentRepository.save(anotherPayment);
    }

    @Test
    public void whenGetPayments_thenReturnAllPayments() {
        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PaymentResponseModel.class)
                .hasSize(2);
    }

    @Test
    public void whenGetPaymentById_thenReturnPayment() {
        Payment payment = paymentRepository.findAll().get(0);

        webTestClient.get().uri(BASE_URL + "/{paymentId}", payment.getPaymentIdentifier().getPaymentId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponseModel.class);
    }

    @Test
    public void whenGetPaymentById_thenPaymentNotFound() {
        webTestClient.get().uri(BASE_URL + "/{paymentId}", "nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenCreatePayment_thenReturnCreatedPayment() {
        PaymentRequestModel paymentRequestModel = PaymentRequestModel.builder()
                .amount(300.0)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.Crypto)
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(paymentRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PaymentResponseModel.class);
    }

    @Test
    public void whenCreatePaymentWithInvalidFields_thenThrowMethodArgumentNotValidException() {
        // Create a PaymentRequestModel with invalid fields
        PaymentRequestModel paymentRequestModel = PaymentRequestModel.builder()
                .amount(0)
                .paymentDate(null) // Null payment date
                .paymentMethod(null) // Null payment method
                .build();

        // Make a POST request with the invalid PaymentRequestModel
        webTestClient.post().uri(BASE_URL)
                .bodyValue(paymentRequestModel)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("$.paymentDate").isEqualTo("Payment date cannot be null")
                .jsonPath("$.paymentMethod").isEqualTo("Payment method cannot be null");
    }

    @Test
    public void whenCreatePaymentWithInvalidPaymentMethod_thenThrowMethodArgumentNotValidException() {
        // Create a JSON payload with an invalid payment method
        String paymentRequestJson = "{"
                + "\"amount\": 100.0,"
                + "\"paymentDate\": \"2022-12-01T00:00:00\","
                + "\"paymentMethod\": \"Invalid method\""
                + "}";

        // Make a POST request with the invalid PaymentRequestModel
        webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paymentRequestJson))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void whenUpdatePayment_thenReturnUpdatedPayment() {
        Payment existingPayment = paymentRepository.findAll().get(0);
        PaymentRequestModel paymentRequestModel = PaymentRequestModel.builder()
                .amount(400.0)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.ETF)
                .build();

        webTestClient.put().uri(BASE_URL + "/{paymentId}", existingPayment.getPaymentIdentifier().getPaymentId())
                .bodyValue(paymentRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponseModel.class);
    }

    @Test
    public void whenUpdateNonExistingPayment_thenThrowNotFoundException() {
        PaymentRequestModel paymentRequestModel = PaymentRequestModel.builder()
                .amount(400.0)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.ETF)
                .build();

        webTestClient.put().uri(BASE_URL + "/{paymentId}", "nonexistent")
                .bodyValue(paymentRequestModel)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeletePayment_thenPaymentIsDeleted() {
        Payment payment = paymentRepository.findAll().get(0);

        webTestClient.delete().uri(BASE_URL + "/{paymentId}", payment.getPaymentIdentifier().getPaymentId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the payment has been deleted
        webTestClient.get().uri(BASE_URL + "/{paymentId}", payment.getPaymentIdentifier().getPaymentId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteNonExistingPayment_thenThrowNotFoundException() {
        webTestClient.delete().uri(BASE_URL + "/{paymentId}", "nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }
}