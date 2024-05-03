package com.example.productsservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();

        // Create and save a category
        Category category = new Category();
        category.setCategoryIdentifier(new CategoryIdentifier());
        category.setName("Test Category");
        categoryRepository.save(category);
    }

    @Test
    public void testFindAll() {
        List<Category> categories = categoryRepository.findAll();
        assertEquals(1, categories.size());
    }

    @Test
    public void whenCategoryExists_testFindByCategoryIdentifier_CategoryId() {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.get(0);
        Optional<Category> foundCategory = categoryRepository.findByCategoryIdentifier_CategoryId(category.getCategoryIdentifier().getCategoryId());
        assertTrue(foundCategory.isPresent());
        assertEquals(category.getCategoryIdentifier().getCategoryId(), foundCategory.get().getCategoryIdentifier().getCategoryId());
    }

    @Test
    public void whenCategoryDoesNotExist_testFindByCategoryIdentifier_CategoryId() {
        Optional<Category> foundCategory = categoryRepository.findByCategoryIdentifier_CategoryId("nonexistent");
        assertFalse(foundCategory.isPresent());
    }

    @Test
    public void testFindAllByCategoryIdentifier_CategoryIdIn() {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.get(0);
        List<Category> foundCategories = categoryRepository.findAllByCategoryIdentifier_CategoryIdIn(Arrays.asList(category.getCategoryIdentifier().getCategoryId()));
        assertEquals(1, foundCategories.size());
    }

    @Test
    public void testDeleteByCategoryIdentifier_CategoryId() {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.get(0);
        categoryRepository.deleteByCategoryIdentifier_CategoryId(category.getCategoryIdentifier().getCategoryId());
        List<Category> remainingCategories = categoryRepository.findAll();
        assertEquals(0, remainingCategories.size());
    }
}