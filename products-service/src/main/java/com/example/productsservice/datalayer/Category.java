package com.example.productsservice.datalayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CategoryIdentifier categoryIdentifier;

    @Column(unique = true)
    @NotBlank(message = "Category name cannot be blank")
    private String name;
}
