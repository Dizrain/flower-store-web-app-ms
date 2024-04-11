package com.example.customersservice.businesslayer;

import com.example.customersservice.presentationlayer.CustomerRequestModel;
import com.example.customersservice.presentationlayer.CustomerResponseModel;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseModel> getAllCustomers();

    CustomerResponseModel getCustomerById(String customerId);

    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);

    CustomerResponseModel updateCustomer(CustomerRequestModel updatedCustomer, String customerId);

    void removeCustomer(String customerId);
}