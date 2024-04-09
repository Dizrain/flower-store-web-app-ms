package com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseModel extends RepresentationModel<ProductResponseModel> {

    String productId;
    String name;
    String description;
    List<String> categoryIds;
    double price;
}
