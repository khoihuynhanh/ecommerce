package com.ecommerce.services;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.responses.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryDTO categoryDTO);

    List<CategoryResponse> getAllCategory();

    CategoryResponse getCategoryById(int id);

    CategoryResponse updateCategory(int id, CategoryDTO categoryDTO);

    CategoryResponse deleteCategory(int id);
}
