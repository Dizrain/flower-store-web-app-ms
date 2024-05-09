package com.example.productsservice.datamapperlayer;

import com.example.productsservice.datalayer.StockItem;
import com.example.productsservice.presentationlayer.StockItemResponseModel;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface StockItemResponseMapper {

    @Mapping(source = "stockItemIdentifier.stockItemId", target = "stockItemId")
    StockItemResponseModel entityToResponseModel(StockItem stockItem);

    List<StockItemResponseModel> entityListToResponseModelList(List<StockItem> stockItems);
}
