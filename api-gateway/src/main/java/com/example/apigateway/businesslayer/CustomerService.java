package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.customerdtos.CustomerRequestModel;
import com.example.apigateway.presentationlayer.customerdtos.CustomerResponseModel;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseModel> getAllCustomers();

    CustomerResponseModel getCustomerById(String customerId);

    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);

    CustomerResponseModel updateCustomer(CustomerRequestModel updatedCustomer, String customerId);

    void removeCustomer(String customerId);
}