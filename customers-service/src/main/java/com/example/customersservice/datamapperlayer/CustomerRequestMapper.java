package com.example.flowerstorewebapp.customermanagementsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.customermanagementsubdomain.datalayer.Customer;
import com.example.flowerstorewebapp.customermanagementsubdomain.datalayer.CustomerIdentifier;
import com.example.flowerstorewebapp.customermanagementsubdomain.presentationlayer.CustomerRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {

    @Mapping(target = "id", ignore = true) // Ignore the ID when mapping from request model to entity
    Customer requestModelToEntity(CustomerRequestModel customerRequestModel, CustomerIdentifier customerIdentifier);
}
