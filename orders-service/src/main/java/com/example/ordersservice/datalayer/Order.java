package com.example.ordersservice.datalayer;

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

    @Embedded
    private CustomerIdentifier customerId; // References the customer who placed the order

    @Embedded
    private PaymentIdentifier paymentId; // References the payment for the order

    @NotNull(message = "Items cannot be null")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id") // This column is in the order_items table
    private List<OrderItem> items; // List of items in the order

    @NotBlank(message = "Shipping address cannot be blank")
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @NotBlank(message = "Billing information cannot be blank")
    @Column(name = "billing_information", nullable = false)
    private String billingInformation;

    @NotNull(message = "Order status cannot be null")
    @Enumerated(EnumType.STRING)
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

        @Column(name = "product_id", nullable = false)
        private ProductIdentifier productId; // References the product in the order item

        // Minimum quantity is 1
        @Min(value = 1, message = "Quantity must be greater than 0")
        @Column(nullable = false)
        private int quantity;
    }
}
