package com.example.customersservice.datamapperlayer;

import com.example.customersservice.datalayer.Customer;
import com.example.customersservice.presentationlayer.CustomerController;
import com.example.customersservice.presentationlayer.CustomerResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CustomerResponseMapper {

    @Mapping(expression = "java(customer.getCustomerIdentifier().getCustomerId())", target = "customerId")
    CustomerResponseModel entityToResponseModel(Customer customer);

    List<CustomerResponseModel> entityListToResponseModelList(List<Customer> customers);

    @AfterMapping
    default void addLinks(Customer customer, @MappingTarget CustomerResponseModel model){
        Link selfLink= linkTo(methodOn(CustomerController.class)
                .getCustomerById(model.getCustomerId()))
                .withSelfRel();
        model.add(selfLink);
    }
}