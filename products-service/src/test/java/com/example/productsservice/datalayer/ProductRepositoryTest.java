package com.example.productsservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create and save a category
        Category category = new Category();
        category.setName("Test Category");
        categoryRepository.save(category);

        // Create and save a product associated with the category
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.00"));
        product.setProductIdentifier(new ProductIdentifier());
        product.setCategories(new HashSet<>(Arrays.asList(category)));
        productRepository.save(product);

        // Create and save another product associated with the category
        Product anotherProduct = new Product();
        anotherProduct.setName("Another Test Product");
        anotherProduct.setPrice(new BigDecimal("15.00"));
        anotherProduct.setProductIdentifier(new ProductIdentifier());
        anotherProduct.setCategories(new HashSet<>(Arrays.asList(category)));
        productRepository.save(anotherProduct);
    }

    @Test
    public void testFindAll() {
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }

    @Test
    public void whenProductExists_testFindByProductIdentifier() {
        List<Product> products = productRepository.findAll();
        Product product = products.get(0);
        Optional<Product> foundProduct = productRepository.findProductByProductIdentifier_ProductId(product.getProductIdentifier().getProductId());
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getProductIdentifier().getProductId(), foundProduct.get().getProductIdentifier().getProductId());
    }

    @Test
    public void whenProductDoesNotExist_testFindByProductIdentifier() {
        Optional<Product> foundProduct = productRepository.findProductByProductIdentifier_ProductId("nonexistent");
        assertFalse(foundProduct.isPresent());
    }

    @Test
    public void testFindAllByCategories_Id() {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.get(0);
        List<Product> products = productRepository.findAllByCategories_Id(category.getId());
        assertEquals(2, products.size());
    }

    @Test
    public void testDeleteByProductIdentifier_ProductId() {
        List<Product> products = productRepository.findAll();
        Product product = products.get(0);
        productRepository.deleteByProductIdentifier_ProductId(product.getProductIdentifier().getProductId());
        List<Product> remainingProducts = productRepository.findAll();
        assertEquals(1, remainingProducts.size());
    }
}