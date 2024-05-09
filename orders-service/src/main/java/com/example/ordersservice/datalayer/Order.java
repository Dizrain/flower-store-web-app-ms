package com.example.ordersservice.datalayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    @NotNull
    private OrderIdentifier orderIdentifier;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    @Embedded
    @NotNull
    private CustomerDetails customerDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderItem> items;

    private double totalPrice;
}
