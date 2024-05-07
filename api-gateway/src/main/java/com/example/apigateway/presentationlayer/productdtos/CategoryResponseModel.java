package com.example.apigateway.presentationlayer.productdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseModel extends RepresentationModel<ProductResponseModel> {
    String categoryId;
    String name;
}