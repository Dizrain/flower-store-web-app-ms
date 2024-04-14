package com.example.productsservice.presentationlayer;

import com.example.productsservice.businesslayer.ProductService;
import com.example.productsservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ProductController.class)
public class ProductControllerUnitTest {

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    private ProductRequestModel productRequestModel;
    private ProductResponseModel productResponseModel;

    @BeforeEach
    public void setup() {
        productRequestModel = ProductRequestModel.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.0))
                .categoryIds(Arrays.asList("cat1", "cat2"))
                .build();

        productResponseModel = ProductResponseModel.builder()
                .productId("prod1")
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .categoryIds(Arrays.asList("cat1", "cat2"))
                .build();
    }

    @Test
    public void getProducts_thenReturnAllProducts() {
        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(productResponseModel));

        ResponseEntity<List<ProductResponseModel>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(productResponseModel, response.getBody().get(0));
    }

    @Test
    public void getProductById_thenReturnProduct() {
        Mockito.when(productService.getProductById(anyString())).thenReturn(productResponseModel);

        ResponseEntity<ProductResponseModel> response = productController.getProductById("prod1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponseModel, response.getBody());
    }

    @Test
    public void whenProductNotFoundOnGet_thenThrowNotFoundException(){
        Mockito.when(productService.getProductById(anyString())).thenThrow(new NotFoundException("Product not found with id prod1"));

        String NOT_FOUND_PRODUCT_ID = "prod1";
        assertThrowsExactly(NotFoundException.class, ()->
                productController.getProductById(NOT_FOUND_PRODUCT_ID));

        verify(productService, times(1)).getProductById(NOT_FOUND_PRODUCT_ID);
    }

    @Test
    public void createProduct_thenReturnCreatedProduct() {
        Mockito.when(productService.addProduct(any(ProductRequestModel.class))).thenReturn(productResponseModel);

        ResponseEntity<ProductResponseModel> response = productController.createProduct(productRequestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(productResponseModel, response.getBody());
    }

    @Test
    public void whenCreateProductWithExistingName_thenThrowDataIntegrityViolationException() {
        Mockito.when(productService.addProduct(any(ProductRequestModel.class)))
                .thenThrow(new DataIntegrityViolationException("Product with this name already exists"));

        assertThrows(DataIntegrityViolationException.class, () -> productController.createProduct(productRequestModel));
    }

    @Test
    public void updateProduct_thenReturnUpdatedProduct() {
        Mockito.when(productService.updateProduct(any(ProductRequestModel.class), anyString())).thenReturn(productResponseModel);

        ResponseEntity<ProductResponseModel> response = productController.updateProduct("prod1", productRequestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(productResponseModel, response.getBody());
    }

    @Test
    public void deleteProduct_thenStatusNoContent() {
        Mockito.doNothing().when(productService).removeProduct(anyString());

        ResponseEntity<Void> response = productController.deleteProduct("prod1");

        assertEquals(204, response.getStatusCodeValue());
    }
}