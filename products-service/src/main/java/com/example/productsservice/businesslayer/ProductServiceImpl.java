package com.example.productsservice.businesslayer;

import com.example.productsservice.datalayer.*;
import com.example.productsservice.datamapperlayer.ProductRequestMapper;
import com.example.productsservice.datamapperlayer.ProductResponseMapper;
import com.example.productsservice.presentationlayer.ProductRequestModel;
import com.example.productsservice.presentationlayer.ProductResponseModel;
import com.example.productsservice.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRequestMapper productRequestMapper;
    private final ProductResponseMapper productResponseMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ProductRequestMapper productRequestMapper,
                              ProductResponseMapper productResponseMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productRequestMapper = productRequestMapper;
        this.productResponseMapper = productResponseMapper;
    }

    @Override
    public List<ProductResponseModel> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productResponseMapper.entityListToResponseModelList(products);
    }

    @Override
    public ProductResponseModel getProductById(String productId) {
        Product product = productRepository.findProductByProductIdentifier_ProductId(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + productId));
        return productResponseMapper.entityToResponseModel(product);
    }

    @Override
    public ProductResponseModel addProduct(ProductRequestModel productRequestModel) {
        Product product = productRequestMapper.requestModelToEntity(productRequestModel, new ProductIdentifier());

        // Fetch and set the category based on the category IDs provided in the request model (category_id)
        List<Category> categoriesList = categoryRepository.findAllByCategoryIdentifier_CategoryIdIn(productRequestModel.getCategoryIds());
        if (categoriesList.size() != productRequestModel.getCategoryIds().size()) {
            throw new NotFoundException("One or more categories not found with the provided IDs");
        }
        Set<Category> categories = new HashSet<>(categoriesList);
        product.setCategories(categories);

        Product savedProduct = productRepository.save(product);
        return productResponseMapper.entityToResponseModel(savedProduct);
    }

    @Override
    public ProductResponseModel updateProduct(ProductRequestModel updatedProductModel, String productId) {
        Product foundProduct = productRepository.findProductByProductIdentifier_ProductId(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + productId));

        // Fetch and update the categories
        List<Category> categoriesList = categoryRepository.findAllByCategoryIdentifier_CategoryIdIn(updatedProductModel.getCategoryIds());
        if (categoriesList.size() != updatedProductModel.getCategoryIds().size()) {
            throw new NotFoundException("One or more categories not found with the provided IDs");
        }
        Set<Category> categories = new HashSet<>(categoriesList);
        foundProduct.setCategories(categories);

        // Map updated properties from the request model to the found product
        // Assuming your ProductRequestMapper has logic to map other updatable fields
        Product updatedProduct = productRequestMapper.requestModelToEntity(updatedProductModel, foundProduct.getProductIdentifier());
        updatedProduct.setId(foundProduct.getId()); // Ensure the ID is preserved
        updatedProduct.setCategories(categories);

        Product savedProduct = productRepository.save(updatedProduct);
        return productResponseMapper.entityToResponseModel(savedProduct);
    }

    @Override
    @Transactional
    public void removeProduct(String productId) {
        productRepository.deleteByProductIdentifier_ProductId(productId);
    }

    @Override
    public List<ProductResponseModel> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findAllByCategories_Id(categoryId);
        return productResponseMapper.entityListToResponseModelList(products);
    }
}
