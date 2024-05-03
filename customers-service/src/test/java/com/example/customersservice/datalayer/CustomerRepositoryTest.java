package com.example.customersservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();

        // Create and save a customer
        Customer customer = new Customer();
        customer.setCustomerIdentifier(new CustomerIdentifier("cust1"));
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setContactNumber("1234567890");
        customer.setAddress("Test Address");
        customerRepository.save(customer);

        // Create and save another customer
        Customer anotherCustomer = new Customer();
        anotherCustomer.setName("Another Test Customer");
        anotherCustomer.setEmail("anotherTest@example.com");
        anotherCustomer.setContactNumber("0987654321");
        anotherCustomer.setAddress("Another Test Address");
        customerRepository.save(anotherCustomer);
    }

    @Test
    public void testFindAll() {
        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    public void whenCustomerExists_testFindByCustomerIdentifier() {
        String validCustomerId = "cust1";

        Optional<Customer> foundCustomer = customerRepository.findCustomerByCustomerIdentifier_CustomerId(validCustomerId);

        assertTrue(foundCustomer.isPresent());
        assertEquals(validCustomerId, foundCustomer.get().getCustomerIdentifier().getCustomerId());
    }

    @Test
    public void whenCustomerDoesNotExist_testFindByCustomerIdentifier() {
        String invalidCustomerId = "invalidId";

        Optional<Customer> foundCustomer = customerRepository.findCustomerByCustomerIdentifier_CustomerId(invalidCustomerId);

        assertFalse(foundCustomer.isPresent());
    }

    @Test
    public void testDeleteByCustomerIdentifier() {
        String validCustomerId = "cust1";

        customerRepository.deleteByCustomerIdentifier_CustomerId(validCustomerId);

        Optional<Customer> foundCustomer = customerRepository.findCustomerByCustomerIdentifier_CustomerId(validCustomerId);
        assertFalse(foundCustomer.isPresent());
    }
}