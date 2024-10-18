package com.ecommerce.services.impls;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.entities.Category;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.mappers.CategoryMapper;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.responses.CategoryResponse;
import com.ecommerce.services.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getName() == null) {
            throw EcommerceException.badRequestException("Category name must be not null");
        }
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw EcommerceException.badRequestException("Category name already exists");
        }
        Category category = CategoryMapper.toCategory(categoryDTO);
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategory() {
        List<Category> list = categoryRepository.findAll();
        if (list.isEmpty()) {
            throw EcommerceException.notFoundException("Category list must be not empty");
        }
        return list.stream().map(CategoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("Not found id"));
        return CategoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(int id, CategoryDTO categoryDTO) {
        if (categoryDTO.getName().isEmpty()) {
            throw EcommerceException.badRequestException("Category name must be not empty");
        }
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("Not found id"));
        existingCategory.setName(categoryDTO.getName());
        Category updateCategory = categoryRepository.save(existingCategory);
        return CategoryMapper.toCategoryResponse(updateCategory);
    }

    @Override
    public CategoryResponse deleteCategory(int id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("Not found id"));
        categoryRepository.delete(existingCategory);
        return CategoryMapper.toCategoryResponse(existingCategory);
    }
}
