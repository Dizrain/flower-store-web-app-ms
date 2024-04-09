package com.example.flowerstorewebapp.inventorymanagementsubdomain.businesslayer;

import com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer.StockItem;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer.StockItemIdentifier;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer.StockItemRepository;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.datamapperlayer.StockItemRequestMapper;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.datamapperlayer.StockItemResponseMapper;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.presentationlayer.StockItemRequestModel;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.presentationlayer.StockItemResponseModel;
import com.example.flowerstorewebapp.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final StockItemRepository stockItemRepository;
    private final StockItemRequestMapper stockItemRequestMapper;
    private final StockItemResponseMapper stockItemResponseMapper;

    @Autowired
    public InventoryServiceImpl(StockItemRepository stockItemRepository,
                                StockItemRequestMapper stockItemRequestMapper,
                                StockItemResponseMapper stockItemResponseMapper) {
        this.stockItemRepository = stockItemRepository;
        this.stockItemRequestMapper = stockItemRequestMapper;
        this.stockItemResponseMapper = stockItemResponseMapper;
    }

    @Override
    public List<StockItemResponseModel> getAllStockItems() {
        List<StockItem> stockItems = stockItemRepository.findAll();
        return stockItemResponseMapper.entityListToResponseModelList(stockItems);
    }

    @Override
    public StockItemResponseModel getStockItemByProductId(String productId) {
        StockItem stockItem = stockItemRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("Stock item not found for product id " + productId));
        return stockItemResponseMapper.entityToResponseModel(stockItem);
    }

    @Override
    public StockItemResponseModel addStockItem(StockItemRequestModel stockItemRequestModel) {
        StockItem stockItem = stockItemRequestMapper.requestModelToEntity(stockItemRequestModel, new StockItemIdentifier());
        StockItem savedStockItem = stockItemRepository.save(stockItem);
        return stockItemResponseMapper.entityToResponseModel(savedStockItem);
    }

    @Override
    public StockItemResponseModel updateStockItem(StockItemRequestModel updatedStockItemModel, String productId) {
        StockItem foundStockItem = stockItemRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("Stock item not found for product id " + productId));

        StockItem updatedStockItem = stockItemRequestMapper.requestModelToEntity(updatedStockItemModel, foundStockItem.getStockItemIdentifier());
        updatedStockItem.setId(foundStockItem.getId()); // Ensure the correct ID is set
        updatedStockItem.setProductId(productId); // Ensure the correct product ID is set

        StockItem savedStockItem = stockItemRepository.save(updatedStockItem);
        return stockItemResponseMapper.entityToResponseModel(savedStockItem);
    }

    @Override
    public void removeStockItem(String productId) {
        stockItemRepository.deleteByProductId(productId);
    }

    @Override
    public StockItemResponseModel reorderStock(String productId) {
        StockItem stockItem = stockItemRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("Stock item not found for product id " + productId));
        // Logic to reorder stock. This could involve updating the stock level and possibly creating a new order to the supplier.
        // For the sake of this example, let's assume we just update the stock level.
        stockItem.setStockLevel(stockItem.getStockLevel() + 10); // Reorder amount example
        StockItem savedStockItem = stockItemRepository.save(stockItem);
        return stockItemResponseMapper.entityToResponseModel(savedStockItem);
    }
}
