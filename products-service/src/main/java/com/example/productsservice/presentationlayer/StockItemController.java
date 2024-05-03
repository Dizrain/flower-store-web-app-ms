package com.example.productsservice.presentationlayer;

import com.example.productsservice.businesslayer.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-items") // Define the base URI for stock items
public class StockItemController {

    private final InventoryService inventoryService;

    @Autowired
    public StockItemController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    public ResponseEntity<List<StockItemResponseModel>> getAllStockItems() {
        List<StockItemResponseModel> stockItems = inventoryService.getAllStockItems();
        return ResponseEntity.ok(stockItems);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<StockItemResponseModel> getStockItemByProductId(@PathVariable String productId) {
        StockItemResponseModel stockItem = inventoryService.getStockItemByProductId(productId);
        return ResponseEntity.ok(stockItem);
    }

    @PostMapping()
    public ResponseEntity<StockItemResponseModel> createStockItem(@RequestBody StockItemRequestModel stockItemRequestModel) {
        StockItemResponseModel savedStockItem = inventoryService.addStockItem(stockItemRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStockItem);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<StockItemResponseModel> updateStockItem(@PathVariable String productId, @RequestBody StockItemRequestModel stockItemRequestModel) {
        StockItemResponseModel updatedStockItem = inventoryService.updateStockItem(stockItemRequestModel, productId);
        return ResponseEntity.ok(updatedStockItem);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteStockItem(@PathVariable String productId) {
        inventoryService.removeStockItem(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{productId}/reorder")
    public ResponseEntity<StockItemResponseModel> reorderStock(@PathVariable String productId) {
        StockItemResponseModel reorderedStockItem = inventoryService.reorderStock(productId);
        return ResponseEntity.ok(reorderedStockItem);
    }
}
