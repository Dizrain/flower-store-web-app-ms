package com.example.customersservice.businesslayer;

import com.example.customersservice.businesslayer.CustomerService;
import com.example.customersservice.datalayer.Customer;
import com.example.customersservice.datalayer.CustomerIdentifier;
import com.example.customersservice.datalayer.CustomerRepository;
import com.example.customersservice.datamapperlayer.CustomerRequestMapper;
import com.example.customersservice.datamapperlayer.CustomerResponseMapper;
import com.example.customersservice.presentationlayer.CustomerRequestModel;
import com.example.customersservice.presentationlayer.CustomerResponseModel;
import com.example.customersservice.utils.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerRequestMapper customerRequestMapper,
                               CustomerResponseMapper customerResponseMapper) {
        this.customerRepository = customerRepository;
        this.customerRequestMapper = customerRequestMapper;
        this.customerResponseMapper = customerResponseMapper;
    }

    @Override
    public List<CustomerResponseModel> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerResponseMapper.entityListToResponseModelList(customers);
    }

    @Override
    public CustomerResponseModel getCustomerById(String customerId) {
        Customer customer = customerRepository.findCustomerByCustomerIdentifier_CustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + customerId));
        return customerResponseMapper.entityToResponseModel(customer);
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        Customer customer = customerRequestMapper.requestModelToEntity(customerRequestModel, new CustomerIdentifier());
        Customer savedCustomer = customerRepository.save(customer);
        return customerResponseMapper.entityToResponseModel(savedCustomer);
    }

    @Override
    public CustomerResponseModel updateCustomer(CustomerRequestModel updatedCustomerModel, String customerId) {
        Customer existingCustomer = customerRepository.findCustomerByCustomerIdentifier_CustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + customerId));

        // Map the request model to the entity while preserving its ID
        Customer updatedCustomer = customerRequestMapper.requestModelToEntity(updatedCustomerModel, existingCustomer.getCustomerIdentifier());
        updatedCustomer.setId(existingCustomer.getId()); // Ensure the correct ID is set

        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return customerResponseMapper.entityToResponseModel(savedCustomer);
    }
    
    @Override
    @Transactional
    public void removeCustomer(String customerId) {
        customerRepository.deleteByCustomerIdentifier_CustomerId(customerId);
    }
}