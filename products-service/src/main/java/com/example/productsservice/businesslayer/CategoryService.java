package com.example.productsservice.businesslayer;

import com.example.productsservice.presentationlayer.CategoryRequestModel;
import com.example.productsservice.presentationlayer.CategoryResponseModel;

import java.util.List;

public interface CategoryService {
    CategoryResponseModel getCategoryById(String categoryId);
    List<CategoryResponseModel> getAllCategories();
    CategoryResponseModel createCategory(CategoryRequestModel categoryRequestModel);
    CategoryResponseModel updateCategory(String categoryId, CategoryRequestModel categoryRequestModel);
    void removeCategory(String categoryId);

}