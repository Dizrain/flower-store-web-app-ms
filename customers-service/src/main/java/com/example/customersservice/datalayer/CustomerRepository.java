package com.example.customersservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByCustomerIdentifier_CustomerId(String customerId);

    void deleteByCustomerIdentifier_CustomerId(String customerId);
}

