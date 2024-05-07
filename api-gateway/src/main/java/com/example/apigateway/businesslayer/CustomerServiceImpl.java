package com.example.apigateway.businesslayer;

import com.example.apigateway.domainclientlayer.CustomerServiceClient;
import com.example.apigateway.presentationlayer.customerdtos.CustomerRequestModel;
import com.example.apigateway.presentationlayer.customerdtos.CustomerResponseModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerServiceClient customerServiceClient;

    @Override
    public List<CustomerResponseModel> getAllCustomers() {
        return  customerServiceClient.getAllCustomers();
    }

    @Override
    public CustomerResponseModel getCustomerById(String customerId) {
        return customerServiceClient.getCustomerById(customerId);
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        return customerServiceClient.addCustomer(customerRequestModel);
    }

    @Override
    public CustomerResponseModel updateCustomer(CustomerRequestModel customerRequestModel, String customerId) {
        return customerServiceClient.updateCustomer(customerId, customerRequestModel);
    }

    @Override
    public void removeCustomer(String customerId) {
        customerServiceClient.removeCustomer(customerId);
    }
}

