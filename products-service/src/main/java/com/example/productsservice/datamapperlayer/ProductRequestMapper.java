package com.example.productsservice.datamapperlayer;

import com.example.productsservice.datalayer.Product;
import com.example.productsservice.datalayer.ProductIdentifier;
import com.example.productsservice.presentationlayer.ProductRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product requestModelToEntity(ProductRequestModel productRequestModel, ProductIdentifier productIdentifier);
}
