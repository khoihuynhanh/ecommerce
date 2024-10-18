package com.ecommerce.mappers;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.entities.Category;
import com.ecommerce.responses.CategoryResponse;

public class CategoryMapper {
    public static Category toCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .build();
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
