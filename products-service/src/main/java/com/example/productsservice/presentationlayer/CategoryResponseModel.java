package com.example.productsservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseModel extends RepresentationModel<ProductResponseModel> {
    String categoryId;
    String name;
}