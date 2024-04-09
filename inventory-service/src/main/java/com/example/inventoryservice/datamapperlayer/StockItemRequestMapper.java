package com.example.flowerstorewebapp.inventorymanagementsubdomain.datamapperlayer;

import com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer.StockItem;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer.StockItemIdentifier;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.presentationlayer.StockItemRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockItemRequestMapper {

    @Mapping(target = "id", ignore = true) // Ignore the ID field for mapping
    StockItem requestModelToEntity(StockItemRequestModel stockItemRequestModel, StockItemIdentifier stockItemIdentifier);
}
