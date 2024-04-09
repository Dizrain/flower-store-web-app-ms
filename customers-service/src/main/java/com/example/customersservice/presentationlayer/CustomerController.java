package com.example.flowerstorewebapp.customermanagementsubdomain.presentationlayer;

import com.example.flowerstorewebapp.customermanagementsubdomain.businesslayer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers") // Define the base URL for customer-related operations
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponseModel>> getAllCustomers() {
        List<CustomerResponseModel> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> getCustomerById(@PathVariable String customerId) {
        CustomerResponseModel customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping()
    public ResponseEntity<CustomerResponseModel> createCustomer(@RequestBody CustomerRequestModel customerRequestModel) {
        CustomerResponseModel savedCustomer = customerService.addCustomer(customerRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> updateCustomer(@PathVariable String customerId, @RequestBody CustomerRequestModel customerRequestModel) {
        CustomerResponseModel updatedCustomer = customerService.updateCustomer(customerRequestModel, customerId);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.removeCustomer(customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
