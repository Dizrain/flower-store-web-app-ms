package com.example.productsservice.presentationlayer;

import com.example.productsservice.datalayer.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StockItemControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StockItemRepository stockItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(StockItemControllerIntegrationTest.class);

    private final String BASE_URL = "/api/v1/stock-items";

    @BeforeEach
    @Transactional
    public void setUp() {
        logger.info("Setting up the test data...");

        stockItemRepository.deleteAll();

        // Create and save a stock item
        StockItem stockItem = new StockItem();
        stockItem.setStockItemIdentifier(new StockItemIdentifier());
        stockItem.setProductId("Test Product");
        stockItem.setStockLevel(10);
        stockItem.setReorderThreshold(5);
        stockItemRepository.save(stockItem);

        logger.info("Test data has been set up.");
    }

    @Test
    public void whenGetStockItems_thenReturnAllStockItems() {
        logger.info("Getting all stock items...");

        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StockItemResponseModel.class)
                .hasSize(1);
    }

    @Test
    public void whenGetStockItemByProductId_thenReturnStockItem() {
        StockItem stockItem = stockItemRepository.findAll().get(0);

        webTestClient.get().uri("/api/v1/stock-items/{productId}", stockItem.getProductId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(StockItemResponseModel.class);
    }

    @Test
    public void whenGetStockItemByNonexistentProductId_thenThrowNotFoundException() {
        webTestClient.get().uri(BASE_URL + "/{productId}", "nonexistent")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(errorMessage -> errorMessage.contains("Stock item not found for product id nonexistent"));
    }

    @Test
    public void whenCreateStockItem_thenReturnCreatedStockItem() {
        StockItemRequestModel stockItemRequestModel = StockItemRequestModel.builder()
                .productId("New Product")
                .stockLevel(20)
                .reorderThreshold(10)
                .build();

        webTestClient.post().uri("/api/v1/stock-items")
                .bodyValue(stockItemRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StockItemResponseModel.class);
    }

    @Test
    public void whenUpdateStockItem_thenReturnUpdatedStockItem() {
        StockItem stockItem = stockItemRepository.findAll().get(0);
        StockItemRequestModel stockItemRequestModel = StockItemRequestModel.builder()
                .productId(stockItem.getProductId())
                .stockLevel(30)
                .reorderThreshold(15)
                .build();

        webTestClient.put().uri("/api/v1/stock-items/{productId}", stockItem.getProductId())
                .bodyValue(stockItemRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StockItemResponseModel.class);
    }

    @Test
    public void whenDeleteStockItem_thenStatusNoContent() {
        StockItem stockItem = stockItemRepository.findAll().get(0);

        webTestClient.delete().uri("/api/v1/stock-items/{productId}", stockItem.getProductId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenReorderStock_thenStockLevelIsUpdated() {
        StockItem stockItem = stockItemRepository.findAll().get(0);

        webTestClient.post().uri(BASE_URL + "/{productId}/reorder", stockItem.getProductId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(StockItemResponseModel.class)
                .value(reorderedStockItem -> assertEquals(stockItem.getStockLevel() + 10, reorderedStockItem.getStockLevel()));    }
}