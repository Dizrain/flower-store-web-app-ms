package com.example.flowerstorewebapp.productmanagementsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.Category;
import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.Product;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.ProductController;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.ProductResponseModel;
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
