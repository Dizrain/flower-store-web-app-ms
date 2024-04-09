package com.example.flowerstorewebapp.inventorymanagementsubdomain.datalayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.ProductIdentifier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_items") // Explicitly name the table
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Lombok annotation to generate a no-arguments constructor
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Private identifier

    @Embedded
    private StockItemIdentifier stockItemIdentifier; // Public identifier

    @NotNull(message = "Product ID cannot be null")
    @Column(name = "product_id")
    private String productId; // Assuming a simple string ID. If using a relational DB, this could be a foreign key.

    @NotNull(message = "Stock level cannot be null")
    @Min(value = 0, message = "Stock level cannot be negative")
    private Integer stockLevel;

    @NotNull(message = "Reorder threshold cannot be null")
    @Min(value = 0, message = "Reorder threshold cannot be negative")
    private Integer reorderThreshold;

    // Optional: Define relationships if using a relational database
    // @ManyToOne
    // @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    // private Product product;

    // Constructor, getters and setters are managed by Lombok through @Data and @NoArgsConstructor annotations

    // Additional business logic or helper methods can be added here as needed
}