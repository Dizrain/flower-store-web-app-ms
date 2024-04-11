package com.example.productsservice.datamapperlayer;

import com.example.productsservice.datalayer.Category;
import com.example.productsservice.datalayer.Product;
import com.example.productsservice.presentationlayer.ProductController;
import com.example.productsservice.presentationlayer.ProductResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProductResponseMapper {

    @Mapping(target = "categoryIds", expression = "java(getCategoryIds(product))")
    @Mapping(expression = "java(product.getProductIdentifier().getProductId())", target = "productId")
    ProductResponseModel entityToResponseModel(Product product);

    List<ProductResponseModel> entityListToResponseModelList(List<Product> products);

    default List<String> getCategoryIds(Product product) {
        List<String> categoryIds = new ArrayList<>();
        for (Category category : product.getCategories()) {
            categoryIds.add(category.getCategoryIdentifier().getCategoryId());
        }
        return categoryIds;
    }

    @AfterMapping
    default void addLinks(Product product, @MappingTarget ProductResponseModel model){
        Link selfLink= linkTo(methodOn(ProductController.class)
                .getProductById(model.getProductId()))
                .withSelfRel();
        model.add(selfLink);
    }
}
