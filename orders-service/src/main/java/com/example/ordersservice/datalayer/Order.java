package com.example.flowerstorewebapp.orderprocessingsubdomain.datalayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders") // Explicitly name the table to avoid conflicts with SQL reserved keywords
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Lombok annotation to generate a no-arguments constructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Private identifier for the order

    @Embedded
    private OrderIdentifier orderIdentifier; // Public identifier

    @NotBlank(message = "Customer ID cannot be blank")
    @Column(name = "customer_id", nullable = false)
    private String customerId; // References the customer who placed the order

    @NotNull(message = "Items cannot be null")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id") // This column is in the order_items table
    private List<OrderItem> items; // List of items in the order

    @NotBlank(message = "Shipping address cannot be blank")
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress; // Shipping address for the order

    @NotBlank(message = "Billing information cannot be blank")
    @Column(name = "billing_information", nullable = false)
    private String billingInformation; // Billing information for the order

    @NotNull(message = "Order status cannot be null")
    @Enumerated(EnumType.STRING) // This annotation is used to store the enum values as String
    @Column(nullable = false)
    private OrderStatus status;

    // Nested OrderItem class
    @Entity
    @Table(name = "order_items")
    @Data
    @NoArgsConstructor
    public static class OrderItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; // Private identifier for the order item

        @NotBlank(message = "Product ID cannot be blank")
        @Column(name = "product_id", nullable = false)
        private String productId; // References the product

        // Minimum quantity is 1
        @Min(value = 1, message = "Quantity must be greater than 0")
        @Column(nullable = false)
        private int quantity; // Quantity of the product ordered

        // Assuming price is tracked at the order item level to account for price changes over time
        @Min(value = 0, message = "Price per item must be greater than or equal to 0")
        @Column(nullable = false)
        private double pricePerItem;
    }
}
