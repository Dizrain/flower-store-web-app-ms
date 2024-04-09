package com.example.flowerstorewebapp.productmanagementsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.Category;
import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.CategoryIdentifier;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {

    @Mapping(target = "id", ignore = true)
    Category requestModelToEntity(CategoryRequestModel categoryRequestModel, CategoryIdentifier categoryIdentifier);
}
