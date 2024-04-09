package com.example.flowerstorewebapp.productmanagementsubdomain.businesslayer;

import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.Category;
import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.CategoryIdentifier;
import com.example.flowerstorewebapp.productmanagementsubdomain.datalayer.CategoryRepository;
import com.example.flowerstorewebapp.productmanagementsubdomain.datamapperlayer.CategoryRequestMapper;
import com.example.flowerstorewebapp.productmanagementsubdomain.datamapperlayer.CategoryResponseMapper;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryRequestModel;
import com.example.flowerstorewebapp.productmanagementsubdomain.presentationlayer.CategoryResponseModel;
import com.example.flowerstorewebapp.utils.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryRequestMapper categoryRequestMapper;
    private final CategoryResponseMapper categoryResponseMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryRequestMapper categoryRequestMapper,
                               CategoryResponseMapper categoryResponseMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryRequestMapper = categoryRequestMapper;
        this.categoryResponseMapper = categoryResponseMapper;
    }

    @Override
    public List<CategoryResponseModel> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryResponseMapper.entityListToResponseModelList(categories);
    }

    @Override
    public CategoryResponseModel getCategoryById(String categoryId) {
        Category category = categoryRepository.findByCategoryIdentifier_CategoryId(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + categoryId));
        return categoryResponseMapper.entityToResponseModel(category);
    }

    @Override
    public CategoryResponseModel createCategory(CategoryRequestModel categoryRequestModel) {
        Category category = categoryRequestMapper.requestModelToEntity(categoryRequestModel, new CategoryIdentifier());
        Category savedCategory = categoryRepository.save(category);
        return categoryResponseMapper.entityToResponseModel(savedCategory);
    }

    @Override
    public CategoryResponseModel updateCategory(String categoryId, CategoryRequestModel categoryRequestModel) {
        Category foundCategory = categoryRepository.findByCategoryIdentifier_CategoryId(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + categoryId));
        Category updatedCategory = categoryRequestMapper.requestModelToEntity(categoryRequestModel, foundCategory.getCategoryIdentifier());
        updatedCategory.setId(foundCategory.getId());
        categoryRepository.save(updatedCategory);
        return categoryResponseMapper.entityToResponseModel(updatedCategory);
    }

    @Override
    @Transactional
    public void removeCategory(String categoryId) {
        categoryRepository.deleteByCategoryIdentifier_CategoryId(categoryId);
    }
}