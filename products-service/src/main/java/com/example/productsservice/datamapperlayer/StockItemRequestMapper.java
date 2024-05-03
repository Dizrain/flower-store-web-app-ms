package com.example.productsservice.datamapperlayer;

import com.example.productsservice.datalayer.StockItem;
import com.example.productsservice.datalayer.StockItemIdentifier;
import com.example.productsservice.presentationlayer.StockItemRequestModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockItemRequestMapper {

    @Mapping(target = "id", ignore = true) // Ignore the ID field for mapping
    StockItem requestModelToEntity(StockItemRequestModel stockItemRequestModel, StockItemIdentifier stockItemIdentifier);
}
