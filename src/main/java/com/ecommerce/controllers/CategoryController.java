package com.ecommerce.controllers;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.responses.CategoryResponse;
import com.ecommerce.services.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> list = categoryService.getAllCategory();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable("id") int id
    ) {
        CategoryResponse result = categoryService.getCategoryById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable("id") int id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryResponse update = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<CategoryResponse> deleteCategory(
            @PathVariable("id") int id
    ) {
        CategoryResponse delete = categoryService.deleteCategory(id);
        return ResponseEntity.ok(delete);
    }
}
