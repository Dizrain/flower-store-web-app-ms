package com.example.flowerstorewebapp.productmanagementsubdomain.businesslayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryRequestModel;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryResponseModel;

import java.util.List;

public interface CategoryService {
    CategoryResponseModel getCategoryById(String categoryId);
    List<CategoryResponseModel> getAllCategories();
    CategoryResponseModel createCategory(CategoryRequestModel categoryRequestModel);
    CategoryResponseModel updateCategory(String categoryId, CategoryRequestModel categoryRequestModel);
    void removeCategory(String categoryId);

}