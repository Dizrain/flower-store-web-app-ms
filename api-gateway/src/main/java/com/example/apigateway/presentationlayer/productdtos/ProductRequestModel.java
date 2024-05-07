package com.example.apigateway.presentationlayer.productdtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRequestModel {
    String name;
    String description;
    List<String> categoryIds;
    BigDecimal price;
    String color;
    String type;
    boolean inSeason;
}