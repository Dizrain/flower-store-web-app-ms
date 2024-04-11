package com.example.productsservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryIdentifier_CategoryId(String categoryId);

    List<Category> findAllByCategoryIdentifier_CategoryIdIn(List<String> categoryIds);

    void deleteByCategoryIdentifier_CategoryId(String categoryId);
}