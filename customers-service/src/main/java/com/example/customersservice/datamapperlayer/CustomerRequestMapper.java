package com.example.customersservice.datamapperlayer;

import com.example.customersservice.datalayer.Customer;
import com.example.customersservice.datalayer.CustomerIdentifier;
import com.example.customersservice.presentationlayer.CustomerRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {

    @Mapping(target = "id", ignore = true) // Ignore the ID when mapping from request model to entity
    Customer requestModelToEntity(CustomerRequestModel customerRequestModel, CustomerIdentifier customerIdentifier);
}
