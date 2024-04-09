package com.example.flowerstorewebapp.inventorymanagementsubdomain.businesslayer;

import com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer.StockItem;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.presentationlayer.StockItemRequestModel;
import com.example.flowerstorewebapp.inventorymanagementsubdomain.presentationlayer.StockItemResponseModel;

import java.util.List;

public interface InventoryService {

    /**
     * Retrieves all stock items from the inventory.
     * @return a list of StockItemResponseModel representing all stock items.
     */
    List<StockItemResponseModel> getAllStockItems();

    /**
     * Retrieves a single stock item by its product ID.
     * @param productId the unique identifier of the product.
     * @return the StockItemResponseModel of the requested stock item.
     */
    StockItemResponseModel getStockItemByProductId(String productId);

    /**
     * Adds a new stock item to the inventory.
     * @param stockItemRequestModel the stock item information to be added.
     * @return the StockItemResponseModel of the added stock item.
     */
    StockItemResponseModel addStockItem(StockItemRequestModel stockItemRequestModel);

    /**
     * Updates an existing stock item.
     * @param updatedStockItem the updated stock item information.
     * @param productId the ID of the product to update in the inventory.
     * @return the StockItemResponseModel of the updated stock item.
     */
    StockItemResponseModel updateStockItem(StockItemRequestModel updatedStockItem, String productId);

    /**
     * Removes a stock item from the inventory.
     * @param productId the ID of the product to remove.
     */
    void removeStockItem(String productId);

    /**
     * Reorders stock for a product.
     * This method might be implemented to automatically trigger when stock levels fall below the reorder threshold.
     * @param productId the ID of the product to reorder.
     * @return the StockItemResponseModel of the stock item with updated stock level.
     */
    StockItemResponseModel reorderStock(String productId);
}
