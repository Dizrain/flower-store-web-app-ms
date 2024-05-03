package com.example.productsservice.presentationlayer;

import com.example.productsservice.businesslayer.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = StockItemController.class)
public class StockItemControllerUnitTest {

    @Autowired
    private StockItemController stockItemController;

    @MockBean
    private InventoryService inventoryService;

    private StockItemRequestModel stockItemRequestModel;
    private StockItemResponseModel stockItemResponseModel;

    @BeforeEach
    public void setup() {
        stockItemRequestModel = StockItemRequestModel.builder()
                .productId("Test Product")
                .stockLevel(10)
                .reorderThreshold(5)
                .build();

        stockItemResponseModel = new StockItemResponseModel();
        stockItemResponseModel.setProductId("Test Product");
        stockItemResponseModel.setStockLevel(10);
        stockItemResponseModel.setReorderThreshold(5);
    }

    @Test
    public void getAllStockItems_thenReturnAllStockItems() {
        Mockito.when(inventoryService.getAllStockItems()).thenReturn(Arrays.asList(stockItemResponseModel));

        ResponseEntity<List<StockItemResponseModel>> response = stockItemController.getAllStockItems();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(stockItemResponseModel, response.getBody().get(0));
    }

    @Test
    public void getStockItemByProductId_thenReturnStockItem() {
        Mockito.when(inventoryService.getStockItemByProductId(anyString())).thenReturn(stockItemResponseModel);

        ResponseEntity<StockItemResponseModel> response = stockItemController.getStockItemByProductId("Test Product");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stockItemResponseModel, response.getBody());
    }

    @Test
    public void createStockItem_thenReturnCreatedStockItem() {
        Mockito.when(inventoryService.addStockItem(any(StockItemRequestModel.class))).thenReturn(stockItemResponseModel);

        ResponseEntity<StockItemResponseModel> response = stockItemController.createStockItem(stockItemRequestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(stockItemResponseModel, response.getBody());
    }

    @Test
    public void updateStockItem_thenReturnUpdatedStockItem() {
        Mockito.when(inventoryService.updateStockItem(any(StockItemRequestModel.class), anyString())).thenReturn(stockItemResponseModel);

        ResponseEntity<StockItemResponseModel> response = stockItemController.updateStockItem("Test Product", stockItemRequestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stockItemResponseModel, response.getBody());
    }

    @Test
    public void deleteStockItem_thenStatusNoContent() {
        Mockito.doNothing().when(inventoryService).removeStockItem(anyString());

        ResponseEntity<Void> response = stockItemController.deleteStockItem("Test Product");

        assertEquals(204, response.getStatusCodeValue());
    }
}