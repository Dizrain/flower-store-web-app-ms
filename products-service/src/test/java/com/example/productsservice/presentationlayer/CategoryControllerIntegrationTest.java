package com.example.productsservice.presentationlayer;

import com.example.productsservice.datalayer.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryControllerIntegrationTest.class);

    private final String BASE_URL = "/api/v1/categories";

    @BeforeEach
    @Transactional
    public void setUp() {
        logger.info("Setting up the test data...");

        categoryRepository.deleteAll();

        // Create and save a category
        Category category = new Category();
        category.setName("Test Category");
        category.setCategoryIdentifier(new CategoryIdentifier());
        categoryRepository.save(category);

        logger.info("Test data has been set up.");
    }

    @Test
    public void whenGetCategories_thenReturnAllCategories() {
        logger.info("Getting all categories...");

        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryResponseModel.class)
                .hasSize(1);
    }

    @Test
    public void whenGetCategoryById_thenReturnCategory() {
        logger.info("Getting a category by ID...");
        Category category = categoryRepository.findAll().get(0);

        webTestClient.get().uri(BASE_URL + "/{categoryId}", category.getCategoryIdentifier().getCategoryId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponseModel.class);
    }

    @Test
    public void whenGetCategoryById_thenCategoryNotFound() {
        logger.info("Getting a category by ID that does not exist...");
        webTestClient.get().uri(BASE_URL + "/{categoryId}", "nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenCreateCategory_thenReturnCreatedCategory() {
        logger.info("Creating a category...");
        CategoryRequestModel categoryRequestModel = CategoryRequestModel.builder()
                .name("New Category")
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(categoryRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryResponseModel.class);
    }

    @Test
    public void whenUpdateCategory_thenReturnUpdatedCategory() {
        logger.info("Updating a category...");
        Category category = categoryRepository.findAll().get(0);
        CategoryRequestModel categoryRequestModel = CategoryRequestModel.builder()
                .name("Updated Category")
                .build();

        webTestClient.put().uri(BASE_URL + "/{categoryId}", category.getCategoryIdentifier().getCategoryId())
                .bodyValue(categoryRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponseModel.class)
                .value(updatedCategory -> updatedCategory.getName().equals("Updated Category"));
    }

    @Test
    public void whenDeleteCategory_thenCategoryIsDeleted() {
        logger.info("Deleting a category...");
        Category category = categoryRepository.findAll().get(0);

        webTestClient.delete().uri(BASE_URL + "/{categoryId}", category.getCategoryIdentifier().getCategoryId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the category has been deleted
        webTestClient.get().uri(BASE_URL + "/{categoryId}", category.getCategoryIdentifier().getCategoryId())
                .exchange()
                .expectStatus().isNotFound();
    }
}