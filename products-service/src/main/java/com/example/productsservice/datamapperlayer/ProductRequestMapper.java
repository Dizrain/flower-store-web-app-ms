package com.example.flowerstorewebapp.productmanagementsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.Product;
import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.ProductIdentifier;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.ProductRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product requestModelToEntity(ProductRequestModel productRequestModel, ProductIdentifier productIdentifier);
}
