package com.example.customersservice.presentationlayer;

import com.example.customersservice.datalayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerRepository customerRepository;

    private final String BASE_URL = "/api/v1/customers";

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
        anotherCustomer.setCustomerIdentifier(new CustomerIdentifier("cust2"));
        anotherCustomer.setName("Another Test Customer");
        anotherCustomer.setEmail("anotherTest@example.com");
        anotherCustomer.setContactNumber("0987654321");
        anotherCustomer.setAddress("Another Test Address");
        customerRepository.save(anotherCustomer);
    }

    @Test
    public void whenGetCustomers_thenReturnAllCustomers() {
        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponseModel.class)
                .hasSize(2);
    }

    @Test
    public void whenGetCustomerById_thenReturnCustomer() {
        Customer customer = customerRepository.findAll().get(0);

        webTestClient.get().uri(BASE_URL + "/{customerId}", customer.getCustomerIdentifier().getCustomerId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponseModel.class);
    }

    @Test
    public void whenGetCustomerById_thenCustomerNotFound() {
        webTestClient.get().uri(BASE_URL + "/{customerId}", "nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenCreateCustomer_thenReturnCreatedCustomer() {
        CustomerRequestModel customerRequestModel = CustomerRequestModel.builder()
                .name("New Customer")
                .email("new@example.com")
                .contactNumber("1112223334")
                .address("New Address")
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(customerRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerResponseModel.class);
    }

    @Test
    public void whenCreateCustomerWithExistingEmail_thenThrowDataIntegrityViolationException() {
        Customer existingCustomer = customerRepository.findAll().get(0);
        CustomerRequestModel customerRequestModel = CustomerRequestModel.builder()
                .name("New Customer")
                .email(existingCustomer.getEmail())
                .contactNumber("1112223334")
                .address("New Address")
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(customerRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void whenUpdateCustomer_thenReturnUpdatedCustomer() {
        Customer existingCustomer = customerRepository.findAll().get(0);
        CustomerRequestModel customerRequestModel = CustomerRequestModel.builder()
                .name("Updated Customer")
                .email(existingCustomer.getEmail())
                .contactNumber(existingCustomer.getContactNumber())
                .address("Updated Address")
                .build();

        webTestClient.put().uri(BASE_URL + "/{customerId}", existingCustomer.getCustomerIdentifier().getCustomerId())
                .bodyValue(customerRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponseModel.class)
                .value(updatedCustomer -> updatedCustomer.getName().equals("Updated Customer"));
    }

    @Test
    public void whenUpdateNonExistingCustomer_thenThrowNotFoundException() {
        CustomerRequestModel customerRequestModel = CustomerRequestModel.builder()
                .name("Non-existing Customer")
                .email("nonexisting@example.com")
                .contactNumber("1112223334")
                .address("Non-existing Address")
                .build();

        webTestClient.put().uri(BASE_URL + "/{customerId}", "nonexistent")
                .bodyValue(customerRequestModel)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteCustomer_thenCustomerIsDeleted() {
        Customer customer = customerRepository.findAll().get(0);

        webTestClient.delete().uri(BASE_URL + "/{customerId}", customer.getCustomerIdentifier().getCustomerId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the customer has been deleted
        webTestClient.get().uri(BASE_URL + "/{customerId}", customer.getCustomerIdentifier().getCustomerId())
                .exchange()
                .expectStatus().isNotFound();
    }
}