package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.productdtos.CategoryRequestModel;
import com.example.apigateway.presentationlayer.productdtos.CategoryResponseModel;
import com.example.apigateway.domainclientlayer.CategoryServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryServiceClient categoryServiceClient;

    @Autowired
    public CategoryServiceImpl(CategoryServiceClient categoryServiceClient) {
        this.categoryServiceClient = categoryServiceClient;
    }

    @Override
    public List<CategoryResponseModel> getAllCategories() {
        return categoryServiceClient.getAllCategories();
    }

    @Override
    public CategoryResponseModel getCategoryById(String categoryId) {
        return categoryServiceClient.getCategoryById(categoryId);
    }

    @Override
    public CategoryResponseModel createCategory(CategoryRequestModel categoryRequestModel) {
        return categoryServiceClient.addCategory(categoryRequestModel);
    }

    @Override
    public CategoryResponseModel updateCategory(String categoryId, CategoryRequestModel categoryRequestModel) {
        return categoryServiceClient.updateCategory(categoryId, categoryRequestModel);
    }

    @Override
    public void removeCategory(String categoryId) {
        categoryServiceClient.removeCategory(categoryId);
    }
}
