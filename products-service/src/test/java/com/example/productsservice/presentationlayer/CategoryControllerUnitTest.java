package com.example.productsservice.presentationlayer;

import com.example.productsservice.businesslayer.CategoryService;
import com.example.productsservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CategoryController.class)
public class CategoryControllerUnitTest {

    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    private CategoryRequestModel categoryRequestModel;
    private CategoryResponseModel categoryResponseModel;

    @BeforeEach
    public void setup() {
        categoryRequestModel = CategoryRequestModel.builder()
                .name("Test Category")
                .build();

        categoryResponseModel = new CategoryResponseModel();
        categoryResponseModel.setCategoryId("cat1");
        categoryResponseModel.setName("Test Category");
    }

    @Test
    public void getAllCategories_thenReturnAllCategories() {
        Mockito.when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryResponseModel));

        ResponseEntity<List<CategoryResponseModel>> response = categoryController.getAllCategories();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(categoryResponseModel, response.getBody().get(0));
    }

    @Test
    public void getCategoryById_thenReturnCategory() {
        Mockito.when(categoryService.getCategoryById(anyString())).thenReturn(categoryResponseModel);

        ResponseEntity<CategoryResponseModel> response = categoryController.getCategoryById("cat1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoryResponseModel, response.getBody());
    }

    @Test
    public void whenCategoryNotFoundOnGet_thenThrowNotFoundException(){
        Mockito.when(categoryService.getCategoryById(anyString())).thenThrow(new NotFoundException("Category not found with id cat1"));

        String NOT_FOUND_CATEGORY_ID = "cat1";
        assertThrowsExactly(NotFoundException.class, ()->
                categoryController.getCategoryById(NOT_FOUND_CATEGORY_ID));

        verify(categoryService, times(1)).getCategoryById(NOT_FOUND_CATEGORY_ID);
    }

    @Test
    public void createCategory_thenReturnCreatedCategory() {
        Mockito.when(categoryService.createCategory(any(CategoryRequestModel.class))).thenReturn(categoryResponseModel);

        ResponseEntity<CategoryResponseModel> response = categoryController.createCategory(categoryRequestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(categoryResponseModel, response.getBody());
    }

    @Test
    public void whenCreateCategoryWithExistingName_thenThrowDataIntegrityViolationException() {
        Mockito.when(categoryService.createCategory(any(CategoryRequestModel.class)))
                .thenThrow(new DataIntegrityViolationException("Category with this name already exists"));

        assertThrows(DataIntegrityViolationException.class, () -> categoryController.createCategory(categoryRequestModel));
    }

    @Test
    public void updateCategory_thenReturnUpdatedCategory() {
        Mockito.when(categoryService.updateCategory(anyString(), any(CategoryRequestModel.class))).thenReturn(categoryResponseModel);

        ResponseEntity<CategoryResponseModel> response = categoryController.updateCategory("cat1", categoryRequestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoryResponseModel, response.getBody());
    }

    @Test
    public void deleteCategory_thenStatusNoContent() {
        Mockito.doNothing().when(categoryService).removeCategory(anyString());

        ResponseEntity<Void> response = categoryController.deleteCategory("cat1");

        assertEquals(204, response.getStatusCodeValue());
    }
}