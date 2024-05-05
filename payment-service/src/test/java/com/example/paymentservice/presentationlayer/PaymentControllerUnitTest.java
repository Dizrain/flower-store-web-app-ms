package com.example.paymentservice.presentationlayer;

import com.example.paymentservice.businesslayer.PaymentService;
import com.example.paymentservice.datalayer.PaymentMethod;
import com.example.paymentservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = PaymentController.class)
public class PaymentControllerUnitTest {

    @Autowired
    private PaymentController paymentController;

    @MockBean
    private PaymentService paymentService;

    private PaymentRequestModel paymentRequestModel;
    private PaymentResponseModel paymentResponseModel;

    @BeforeEach
    public void setup() {
        paymentRequestModel = PaymentRequestModel.builder()
                .amount(100.0)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CreditCard)
                .build();

        paymentResponseModel = new PaymentResponseModel("pay1", 100.0, LocalDateTime.now(), PaymentMethod.CreditCard);
    }

    @Test
    public void getPayments_thenReturnAllPayments() {
        Mockito.when(paymentService.getPayments()).thenReturn(Arrays.asList(paymentResponseModel));

        ResponseEntity<List<PaymentResponseModel>> response = paymentController.getPayments();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(paymentResponseModel, response.getBody().get(0));
    }

    @Test
    public void getPaymentByPaymentId_thenReturnPayment() {
        Mockito.when(paymentService.getPaymentByPaymentId(anyString())).thenReturn(paymentResponseModel);

        ResponseEntity<PaymentResponseModel> response = paymentController.getPaymentByPaymentId("pay1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(paymentResponseModel, response.getBody());
    }

    @Test
    public void whenPaymentNotFoundOnGet_thenThrowNotFoundException() {
        Mockito.when(paymentService.getPaymentByPaymentId(anyString())).thenThrow(new NotFoundException("Payment not found with id pay1"));

        String NOT_FOUND_PAYMENT_ID = "pay1";
        assertThrowsExactly(NotFoundException.class, () ->
                paymentController.getPaymentByPaymentId(NOT_FOUND_PAYMENT_ID));

        verify(paymentService, times(1)).getPaymentByPaymentId(NOT_FOUND_PAYMENT_ID);
    }

    @Test
    public void addPayment_thenReturnCreatedPayment() {
        Mockito.when(paymentService.addPayment(any(PaymentRequestModel.class))).thenReturn(paymentResponseModel);

        ResponseEntity<PaymentResponseModel> response = paymentController.addPayment(paymentRequestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(paymentResponseModel, response.getBody());
    }

    @Test
    public void updatePayment_thenReturnUpdatedPayment() {
        Mockito.when(paymentService.updatePayment(any(PaymentRequestModel.class), anyString())).thenReturn(paymentResponseModel);

        ResponseEntity<PaymentResponseModel> response = paymentController.updatePayment(paymentRequestModel, "pay1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(paymentResponseModel, response.getBody());
    }

    @Test
    public void removePayment_thenStatusNoContent() {
        Mockito.doNothing().when(paymentService).removePayment(anyString());

        ResponseEntity<Void> response = paymentController.deletePayment("pay1");

        assertEquals(204, response.getStatusCodeValue());
    }
}