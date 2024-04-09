package com.example.flowerstorewebapp.productmanagementsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.Category;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryController;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CategoryResponseMapper {

    @Mapping(expression = "java(category.getCategoryIdentifier().getCategoryId())", target = "categoryId")
    CategoryResponseModel entityToResponseModel(Category category);

    List<CategoryResponseModel> entityListToResponseModelList(List<Category> categories);

    @AfterMapping
    default void addLinks(Category category, @MappingTarget CategoryResponseModel model){
        Link selfLink= linkTo(methodOn(CategoryController.class)
                .getCategoryById(model.getCategoryId()))
                .withSelfRel();
        model.add(selfLink);
    }
}
