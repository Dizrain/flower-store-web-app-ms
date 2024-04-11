package com.example.productsservice.businesslayer;

import com.example.productsservice.datalayer.Category;
import com.example.productsservice.datalayer.Product;
import com.example.productsservice.presentationlayer.ProductRequestModel;
import com.example.productsservice.presentationlayer.ProductResponseModel;

import java.util.List;

public interface ProductService {

    /**
     * Retrieves all products from the product catalog.
     * @return a list of ProductResponseModel representing all products.
     */
    List<ProductResponseModel> getAllProducts();

    /**
     * Retrieves a single product by its ID.
     * @param productId the unique identifier of the product.
     * @return the ProductResponseModel of the requested product.
     */
    ProductResponseModel getProductById(String productId);

    /**
     * Adds a new product to the product catalog.
     * @param productRequestModel the product information to be added.
     * @return the ProductResponseModel of the added product.
     */
    ProductResponseModel addProduct(ProductRequestModel productRequestModel);

    /**
     * Updates an existing product.
     * @param updatedProduct the updated product information.
     * @param productId the ID of the product to update.
     * @return the ProductResponseModel of the updated product.
     */
    ProductResponseModel updateProduct(ProductRequestModel updatedProduct, String productId);

    /**
     * Removes a product from the product catalog.
     * @param productId the ID of the product to remove.
     */
    void removeProduct(String productId);

    /**
     * Retrieves products filtered by category ID.
     * @param categoryId the ID of the category to filter by.
     * @return a list of ProductResponseModel matching the specified category ID.
     */
    List<ProductResponseModel> getProductsByCategory(Long categoryId);
}