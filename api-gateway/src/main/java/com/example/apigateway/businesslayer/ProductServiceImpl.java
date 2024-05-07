package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.productdtos.ProductRequestModel;
import com.example.apigateway.presentationlayer.productdtos.ProductResponseModel;
import com.example.apigateway.domainclientlayer.ProductServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductServiceClient productServiceClient;

    @Autowired
    public ProductServiceImpl(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @Override
    public List<ProductResponseModel> getAllProducts() {
        return productServiceClient.getAllProducts();
    }

    @Override
    public ProductResponseModel getProductById(String productId) {
        return productServiceClient.getProductById(productId);
    }

    @Override
    public ProductResponseModel addProduct(ProductRequestModel productRequestModel) {
        return productServiceClient.addProduct(productRequestModel);
    }

    @Override
    public ProductResponseModel updateProduct(ProductRequestModel updatedProductModel, String productId) {
        return productServiceClient.updateProduct(productId, updatedProductModel);
    }

    @Override
    public void removeProduct(String productId) {
        productServiceClient.removeProduct(productId);
    }

    @Override
    public List<ProductResponseModel> getProductsByCategory(Long categoryId) {
        return productServiceClient.getProductsByCategory(categoryId);
    }
}
