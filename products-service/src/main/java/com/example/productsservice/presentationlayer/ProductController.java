package com.example.productsservice.presentationlayer;

import com.example.productsservice.businesslayer.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseModel>> getAllProducts() {
        List<ProductResponseModel> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseModel> getProductById(@PathVariable String productId) {
        ProductResponseModel product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping()
    public ResponseEntity<ProductResponseModel> createProduct(@RequestBody ProductRequestModel productRequestModel) {
        ProductResponseModel savedProduct = productService.addProduct(productRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseModel> updateProduct(@PathVariable String productId, @RequestBody ProductRequestModel productRequestModel) {
        ProductResponseModel updatedProduct = productService.updateProduct(productRequestModel, productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.removeProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
