package com.example.customersservice.presentationlayer;

import com.example.customersservice.businesslayer.CustomerService;
import com.example.customersservice.utils.exceptions.NotFoundException;
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

@SpringBootTest(classes = CustomerController.class)
public class CustomerControllerUnitTest {

    @Autowired
    private CustomerController customerController;

    @MockBean
    private CustomerService customerService;

    private CustomerRequestModel customerRequestModel;
    private CustomerResponseModel customerResponseModel;

    @BeforeEach
    public void setup() {
        customerRequestModel = CustomerRequestModel.builder()
                .name("Test Customer")
                .email("test@example.com")
                .contactNumber("1234567890")
                .address("Test Address")
                .build();

        customerResponseModel = CustomerResponseModel.builder()
                .customerId("cust1")
                .name("Test Customer")
                .email("test@example.com")
                .contactNumber("1234567890")
                .address("Test Address")
                .build();
    }

    @Test
    public void getAllCustomers_thenReturnAllCustomers() {
        Mockito.when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customerResponseModel));

        ResponseEntity<List<CustomerResponseModel>> response = customerController.getAllCustomers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(customerResponseModel, response.getBody().get(0));
    }

    @Test
    public void getCustomerById_thenReturnCustomer() {
        Mockito.when(customerService.getCustomerById(anyString())).thenReturn(customerResponseModel);

        ResponseEntity<CustomerResponseModel> response = customerController.getCustomerById("cust1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    public void whenCustomerNotFoundOnGet_thenThrowNotFoundException(){
        Mockito.when(customerService.getCustomerById(anyString())).thenThrow(new NotFoundException("Customer not found with id cust1"));

        String NOT_FOUND_CUSTOMER_ID = "cust1";
        assertThrowsExactly(NotFoundException.class, ()->
                customerController.getCustomerById(NOT_FOUND_CUSTOMER_ID));

        verify(customerService, times(1)).getCustomerById(NOT_FOUND_CUSTOMER_ID);
    }

    @Test
    public void createCustomer_thenReturnCreatedCustomer() {
        Mockito.when(customerService.addCustomer(any(CustomerRequestModel.class))).thenReturn(customerResponseModel);

        ResponseEntity<CustomerResponseModel> response = customerController.createCustomer(customerRequestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    public void updateCustomer_thenReturnUpdatedCustomer() {
        Mockito.when(customerService.updateCustomer(any(CustomerRequestModel.class), anyString())).thenReturn(customerResponseModel);

        ResponseEntity<CustomerResponseModel> response = customerController.updateCustomer("cust1", customerRequestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customerResponseModel, response.getBody());
    }

    @Test
    public void deleteCustomer_thenStatusNoContent() {
        Mockito.doNothing().when(customerService).removeCustomer(anyString());

        ResponseEntity<Void> response = customerController.deleteCustomer("cust1");

        assertEquals(204, response.getStatusCodeValue());
    }
}