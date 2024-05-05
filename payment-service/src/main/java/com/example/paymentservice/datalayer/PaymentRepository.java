package com.example.paymentservice.datalayer;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment findByPaymentIdentifier_PaymentId(String paymentId);
}