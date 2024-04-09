package com.example.flowerstorewebapp.customermanagementsubdomain.businesslayer;

import com.example.flowerstorewebapp.customermanagementsubdomain.presentationlayer.CustomerRequestModel;
import com.example.flowerstorewebapp.customermanagementsubdomain.presentationlayer.CustomerResponseModel;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseModel> getAllCustomers();

    CustomerResponseModel getCustomerById(String customerId);

    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);

    CustomerResponseModel updateCustomer(CustomerRequestModel updatedCustomer, String customerId);

    void removeCustomer(String customerId);
}