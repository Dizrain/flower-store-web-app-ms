package com.example.inventoryservice.datamapperlayer;

import com.example.inventoryservice.datalayer.StockItem;
import com.example.inventoryservice.datalayer.StockItemIdentifier;
import com.example.inventoryservice.presentationlayer.StockItemRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockItemRequestMapper {

    @Mapping(target = "id", ignore = true) // Ignore the ID field for mapping
    StockItem requestModelToEntity(StockItemRequestModel stockItemRequestModel, StockItemIdentifier stockItemIdentifier);
}
