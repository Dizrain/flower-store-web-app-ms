package com.example.paymentservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

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
    public void testFindAll() {
        List<Payment> payments = paymentRepository.findAll();
        assertEquals(2, payments.size());
    }

    @Test
    public void whenPaymentExists_testFindByPaymentIdentifier() {
        String validPaymentId = paymentRepository.findAll().get(0).getPaymentIdentifier().getPaymentId();

        Payment foundPayment = paymentRepository.findByPaymentIdentifier_PaymentId(validPaymentId);

        assertNotNull(foundPayment);
        assertEquals(validPaymentId, foundPayment.getPaymentIdentifier().getPaymentId());
    }

    @Test
    public void whenPaymentDoesNotExist_testFindByPaymentIdentifier() {
        String invalidPaymentId = "invalidId";

        Payment foundPayment = paymentRepository.findByPaymentIdentifier_PaymentId(invalidPaymentId);

        assertNull(foundPayment);
    }
}