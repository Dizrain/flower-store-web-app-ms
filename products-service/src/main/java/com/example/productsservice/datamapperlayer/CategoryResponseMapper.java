package com.example.productsservice.datamapperlayer;

import com.example.productsservice.datalayer.Category;
import com.example.productsservice.presentationlayer.CategoryResponseModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CategoryResponseMapper {

    @Mapping(expression = "java(category.getCategoryIdentifier().getCategoryId())", target = "categoryId")
    CategoryResponseModel entityToResponseModel(Category category);

    List<CategoryResponseModel> entityListToResponseModelList(List<Category> categories);
}
