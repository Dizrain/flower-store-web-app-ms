package com.example.ordersservice.datalayer;

import jakarta.persistence.*;
import jakarta.validation.Valid;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NotNull
    private OrderIdentifier orderIdentifier;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    @Embedded
    @NotNull
    @Valid
    private CustomerDetails customerDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    @Valid
    private Set<OrderItem> items;

    private double totalPrice;
}
