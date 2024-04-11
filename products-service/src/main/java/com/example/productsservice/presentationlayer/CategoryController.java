package com.example.productsservice.presentationlayer;

import com.example.productsservice.businesslayer.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseModel> getCategoryById(@PathVariable String categoryId) {
        CategoryResponseModel category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponseModel>> getAllCategories() {
        List<CategoryResponseModel> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping()
    public ResponseEntity<CategoryResponseModel> createCategory(@RequestBody CategoryRequestModel categoryRequestModel) {
        CategoryResponseModel savedCategory = categoryService.createCategory(categoryRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseModel> updateCategory(@PathVariable String categoryId, @RequestBody CategoryRequestModel categoryRequestModel) {
        CategoryResponseModel updatedCategory = categoryService.updateCategory(categoryId, categoryRequestModel);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {
        categoryService.removeCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}