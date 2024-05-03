package com.example.customersservice.datalayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers") // Explicitly naming the table
@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-arguments constructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Private identifier

    @Embedded
    private CustomerIdentifier customerIdentifier; // Public identifier

    @NotBlank(message = "Customer name cannot be blank")
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Contact number cannot be blank")
    private String contactNumber;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    // TODO: Delete later if not needed
    // Consider additional fields for preferences and history if necessary
    // For instance, a relationship to an OrderHistory entity could be represented as:
    // @OneToMany(mappedBy = "customer")
    // private List<OrderHistory> orderHistory;
}
