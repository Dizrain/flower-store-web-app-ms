package com.example.productsservice.presentationlayer;

import com.example.productsservice.datalayer.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductControllerIntegrationTest.class);

    private final String BASE_URL = "/api/v1/products";

    @BeforeEach
    @Transactional
    public void setUp() {
        logger.info("Setting up the test data...");

        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create and save a category
        Category category = new Category();
        category.setName("Test Category");
        category.setCategoryIdentifier(new CategoryIdentifier());
        categoryRepository.save(category);

        // Create and save a product associated with the category
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.00"));
        product.setProductIdentifier(new ProductIdentifier());
        product.setCategories(new HashSet<>(Arrays.asList(category)));
        product.setColor("Red");
        product.setType("Rose");
        product.setInSeason(true);
        productRepository.save(product);

        // Create and save another product associated with the category
        Product anotherProduct = new Product();
        anotherProduct.setName("Another Test Product");
        anotherProduct.setPrice(new BigDecimal("15.00"));
        anotherProduct.setProductIdentifier(new ProductIdentifier());
        anotherProduct.setCategories(new HashSet<>(Arrays.asList(category)));
        anotherProduct.setColor("White");
        anotherProduct.setType("Lily");
        anotherProduct.setInSeason(false);
        productRepository.save(anotherProduct);

        logger.info("Test data has been set up.");
    }

    @Test
    public void whenGetProducts_thenReturnAllProducts() {
        logger.info("Getting all products...");
        productRepository.findAll().forEach(createdProduct -> {
            logger.info("Product{}", createdProduct.getName());
        });

        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponseModel.class)
                .hasSize(2);
    }

    @Test
    public void whenGetProductById_thenReturnProduct() {
        logger.info("Getting a product by ID...");
        Product product = productRepository.findAll().get(0);

        webTestClient.get().uri(BASE_URL + "/{productId}", product.getProductIdentifier().getProductId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseModel.class);
    }

    @Test
    public void whenGetProductById_thenProductNotFound() {
        logger.info("Getting a product by ID that does not exist...");
        webTestClient.get().uri(BASE_URL + "/{productId}", "nonexistent")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenCreateProduct_thenReturnCreatedProduct() {
        logger.info("Creating a product...");
        // Get existing category
        Category category = categoryRepository.findAll().get(0);
        // Use builder pattern to create a new product request model
        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .name("New Product")
                .price(new BigDecimal("20.00"))
                .categoryIds(Arrays.asList(category.getCategoryIdentifier().getCategoryId()))
                .color("Blue")
                .type("Tulip")
                .inSeason(true)
                .build();


        webTestClient.post().uri(BASE_URL)
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponseModel.class);
    }

    @Test
    public void whenCreateProductWithExistingName_thenThrowDataIntegrityViolationException() {
        logger.info("Creating a product with an existing name...");
        // Get existing category and product
        Category category = categoryRepository.findAll().get(0);
        Product product = productRepository.findAll().get(0);
        // Create a product
        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .name(product.getName())
                .description("Test Description")
                .price(new BigDecimal("100.0"))
                .categoryIds(Arrays.asList(category.getCategoryIdentifier().getCategoryId()))
                .color("Green")
                .type("Orchid")
                .inSeason(false)
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void whenCreateProductWithNonexistentCategory_thenThrowNotFoundException() {
        logger.info("Creating a product with a nonexistent category...");
        // Use builder pattern to create a new product request model
        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .name("New Product")
                .price(new BigDecimal("20.00"))
                .categoryIds(Arrays.asList("nonexistent"))
                .color("Blue")
                .type("Tulip")
                .inSeason(true)
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(errorMessage -> errorMessage.contains("One or more categories not found with the provided IDs"));
    }

    @Test
    public void whenUpdateProduct_thenReturnUpdatedProduct() {
        logger.info("Updating a product...");
        // Get existing product
        Product product = productRepository.findAll().get(0);
        // Use builder pattern to create a new product request model
        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .name("Updated Product")
                .price(product.getPrice())
                .categoryIds(product.getCategories().stream().map(category -> category.getCategoryIdentifier().getCategoryId()).toList())
                .color(product.getColor())
                .type(product.getType())
                .inSeason(product.isInSeason())
                .build();

        webTestClient.put().uri(BASE_URL + "/{productId}", product.getProductIdentifier().getProductId())
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseModel.class)
                // Verify the updated product name
                .value(updatedProduct -> updatedProduct.getName().equals("Updated Product"));
    }

    @Test
    public void whenUpdateProductWithNonexistentCategory_thenThrowNotFoundException() {
        logger.info("Updating a product with a nonexistent category...");
        // Get existing product
        Product product = productRepository.findAll().get(0);
        // Use builder pattern to create a new product request model
        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .name("Updated Product")
                .price(product.getPrice())
                .categoryIds(Arrays.asList("nonexistent"))
                .color(product.getColor())
                .type(product.getType())
                .inSeason(product.isInSeason())
                .build();

        webTestClient.put().uri(BASE_URL + "/{productId}", product.getProductIdentifier().getProductId())
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(errorMessage -> errorMessage.contains("One or more categories not found with the provided IDs"));
    }

    @Test
    public void whenDeleteProduct_thenProductIsDeleted() {
        logger.info("Deleting a product...");
        Product product = productRepository.findAll().get(0);

        webTestClient.delete().uri(BASE_URL + "/{productId}", product.getProductIdentifier().getProductId())
                .exchange()
                .expectStatus().isNoContent();

        // Verify the product has been deleted
        webTestClient.get().uri(BASE_URL + "/{productId}", product.getProductIdentifier().getProductId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenGetProductsByCategory_thenReturnProductsInCategory() {
        logger.info("Getting products by category...");
        // Get existing category
        Category category = categoryRepository.findAll().get(0);

        webTestClient.get().uri(BASE_URL + "/category/{categoryId}", category.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponseModel.class)
                .hasSize(2);
    }

    @Test
    public void whenCreateProductWithNullName_thenThrowConstraintViolationException() {
        logger.info("Creating a product with a null name...");
        // Get existing category
        Category category = categoryRepository.findAll().get(0);
        // Use builder pattern to create a new product request model
        ProductRequestModel productRequestModel = ProductRequestModel.builder()
                .name(null)
                .price(new BigDecimal("20.00"))
                .categoryIds(Arrays.asList(category.getCategoryIdentifier().getCategoryId()))
                .color("Blue")
                .type("Tulip")
                .inSeason(true)
                .build();

        webTestClient.post().uri(BASE_URL)
                .bodyValue(productRequestModel)
                .exchange()
                .expectStatus().isEqualTo(422);
    }
}