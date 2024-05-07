package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.productdtos.StockItemRequestModel;
import com.example.apigateway.presentationlayer.productdtos.StockItemResponseModel;
import com.example.apigateway.domainclientlayer.InventoryServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryServiceClient stockItemServiceClient;

    @Autowired
    public InventoryServiceImpl(InventoryServiceClient stockItemServiceClient) {
        this.stockItemServiceClient = stockItemServiceClient;
    }

    @Override
    public List<StockItemResponseModel> getAllStockItems() {
        return stockItemServiceClient.getAllStockItems();
    }

    @Override
    public StockItemResponseModel getStockItemByProductId(String productId) {
        return stockItemServiceClient.getStockItemByProductId(productId);
    }

    @Override
    public StockItemResponseModel addStockItem(StockItemRequestModel stockItemRequestModel) {
        return stockItemServiceClient.addStockItem(stockItemRequestModel);
    }

    @Override
    public StockItemResponseModel updateStockItem(StockItemRequestModel updatedStockItemModel, String productId) {
       return stockItemServiceClient.updateStockItem(productId, updatedStockItemModel);
    }

    @Override
    public void removeStockItem(String productId) {
        stockItemServiceClient.removeStockItem(productId);
    }

    @Override
    public StockItemResponseModel reorderStock(String productId) {
        return stockItemServiceClient.reorderStock(productId);
    }
}
