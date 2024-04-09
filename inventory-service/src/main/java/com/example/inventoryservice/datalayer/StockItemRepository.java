package com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    // Find a stock item by its associated product ID
    Optional<StockItem> findByProductId(String productId);

    // Delete a stock item by its associated product ID
    void deleteByProductId(String productId);

    // Add more custom queries below if needed
}