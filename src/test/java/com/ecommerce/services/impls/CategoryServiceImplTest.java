package com.ecommerce.services.impls;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.entities.Category;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.responses.CategoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class})
class CategoryServiceImplTest {
    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    void createCategory_with_name_is_null() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name(null)
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.createCategory(categoryDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Category name must be not null", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.never())
                .save(any(Category.class));
    }

    @Test
    void createCategory_with_existing_name() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("existingName")
                .build();

        Mockito.when(categoryRepository.existsByName("existingName"))
                .thenReturn(true);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.createCategory(categoryDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Category name already exists", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.never())
                .save(any(Category.class));
    }

    @Test
    void createCategory_success() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("name")
                .build();

        Category savedCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();

        Mockito.when(categoryRepository.existsByName("name"))
                .thenReturn(false);
        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);

        CategoryResponse actual = categoryService.createCategory(categoryDTO);

        Assertions.assertEquals("name", actual.getName());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(any(Category.class));
    }

    @Test
    void getAllCategory_success() {
        Category category1 = Category.builder()
                .id(1)
                .name("name1")
                .build();
        Category category2 = Category.builder()
                .id(2)
                .name("name2")
                .build();
        List<Category> list = List.of(category1, category2);

        Mockito.when(categoryRepository.findAll())
                .thenReturn(list);

        List<CategoryResponse> actual = categoryService.getAllCategory();

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("name1", actual.get(0).getName());
        Assertions.assertEquals("name2", actual.get(1).getName());

        Mockito.verify(categoryRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void getAllCategory_empty() {
        Mockito.when(categoryRepository.findAll())
                .thenReturn(Collections.emptyList());

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.getAllCategory()
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Category list must be not empty", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void getCategoryById_notFound() {
        int id = 1;

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.getCategoryById(id)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Not found id", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void getCategoryById_success() {
        int id = 1;
        Category existingCategory = Category.builder()
                .id(id)
                .name("name")
                .build();
        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(existingCategory));
        CategoryResponse actual = categoryService.getCategoryById(id);
        Assertions.assertEquals(id, actual.getId());
        Assertions.assertEquals(existingCategory.getName(), actual.getName());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void updateCategory_with_empty_name() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("")
                .build();
        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.updateCategory(1, categoryDTO)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Category name must be not empty", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.never())
                .save(any(Category.class));
    }

    @Test
    void updateCategory_notFoundId() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("name")
                .build();

        Mockito.when(categoryRepository.findById(1))
                .thenReturn(Optional.empty());

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.updateCategory(1, categoryDTO)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Not found id", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(1);
    }

    @Test
    void updateCategory_success() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("name")
                .build();
        Category category = Category.builder()
                .id(1)
                .name("existingName")
                .build();

        Mockito.when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponse actual = categoryService.updateCategory(1, categoryDTO);

        Assertions.assertEquals(category.getName(), actual.getName());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(1);
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(any(Category.class));
    }

    @Test
    void deleteCategory_notFoundId() {
        Mockito.when(categoryRepository.findById(1))
                .thenReturn(Optional.empty());
        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> categoryService.deleteCategory(1)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Not found id", actualThrow.getError().getMessage());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .findById(1);
    }

    @Test
    void deleteCategory_success() {
        Category category = Category.builder()
                .id(1)
                .name("name")
                .build();
        Mockito.when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));
        CategoryResponse actual = categoryService.deleteCategory(1);
        Assertions.assertEquals(category.getId(), actual.getId());
        Mockito.verify(categoryRepository, Mockito.times(1))
                .delete(category);
    }
}