package com.example.productsservice.datamapperlayer;

import com.example.productsservice.datalayer.Category;
import com.example.productsservice.datalayer.CategoryIdentifier;
import com.example.productsservice.presentationlayer.CategoryRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {

    @Mapping(target = "id", ignore = true)
    Category requestModelToEntity(CategoryRequestModel categoryRequestModel, CategoryIdentifier categoryIdentifier);
}
