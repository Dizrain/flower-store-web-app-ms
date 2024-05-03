package com.example.productsservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StockItemRepositoryTest {

    @Autowired
    private StockItemRepository stockItemRepository;

    @BeforeEach
    public void setUp() {
        stockItemRepository.deleteAll();

        // Create and save a stock item
        StockItem stockItem = new StockItem();
        stockItem.setStockItemIdentifier(new StockItemIdentifier());
        stockItem.setProductId("Test Product ID");
        stockItem.setStockLevel(10);
        stockItem.setReorderThreshold(5);
        stockItemRepository.save(stockItem);
    }

    @Test
    public void testFindAll() {
        var stockItems = stockItemRepository.findAll();
        assertEquals(1, stockItems.size());
    }

    @Test
    public void whenStockItemExists_testFindByProductId() {
        var stockItems = stockItemRepository.findAll();
        var stockItem = stockItems.get(0);
        Optional<StockItem> foundStockItem = stockItemRepository.findByProductId(stockItem.getProductId());
        assertTrue(foundStockItem.isPresent());
        assertEquals(stockItem.getProductId(), foundStockItem.get().getProductId());
    }

    @Test
    public void whenStockItemDoesNotExist_testFindByProductId() {
        Optional<StockItem> foundStockItem = stockItemRepository.findByProductId("nonexistent");
        assertFalse(foundStockItem.isPresent());
    }

    @Test
    public void testDeleteByProductId() {
        var stockItems = stockItemRepository.findAll();
        var stockItem = stockItems.get(0);
        stockItemRepository.deleteByProductId(stockItem.getProductId());
        var remainingStockItems = stockItemRepository.findAll();
        assertEquals(0, remainingStockItems.size());
    }
}