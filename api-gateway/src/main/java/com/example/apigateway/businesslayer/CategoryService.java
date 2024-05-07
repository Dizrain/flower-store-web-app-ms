package com.example.apigateway.businesslayer;

import com.example.apigateway.presentationlayer.productdtos.CategoryRequestModel;
import com.example.apigateway.presentationlayer.productdtos.CategoryResponseModel;

import java.util.List;

public interface CategoryService {
    CategoryResponseModel getCategoryById(String categoryId);
    List<CategoryResponseModel> getAllCategories();
    CategoryResponseModel createCategory(CategoryRequestModel categoryRequestModel);
    CategoryResponseModel updateCategory(String categoryId, CategoryRequestModel categoryRequestModel);
    void removeCategory(String categoryId);

}