package com.example.inventoryservice.datamapperlayer;

import com.example.inventoryservice.datalayer.StockItem;
import com.example.inventoryservice.presentationlayer.StockItemController;
import com.example.inventoryservice.presentationlayer.StockItemResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface StockItemResponseMapper {

    @Mapping(source = "stockItemIdentifier.stockItemId", target = "stockItemId")
    StockItemResponseModel entityToResponseModel(StockItem stockItem);

    List<StockItemResponseModel> entityListToResponseModelList(List<StockItem> stockItems);

    @AfterMapping
    default void addLinks(@MappingTarget StockItemResponseModel model, StockItem stockItem) {
        Link selfLink = linkTo(methodOn(StockItemController.class)
                .getStockItemByProductId(model.getProductId()))
                .withSelfRel();
        model.add(selfLink);
    }
}
