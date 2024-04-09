package com.example.flowerstorewebapp.inventorymanagementsubdomain.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockItemResponseModel extends RepresentationModel<StockItemResponseModel> {

    String stockItemId; // The unique identifier of the stock item
    String productId; // The unique identifier of the product this stock item corresponds to
    int stockLevel; // The current stock level of the product
    int reorderThreshold; // The stock level at which a reorder is triggered
}