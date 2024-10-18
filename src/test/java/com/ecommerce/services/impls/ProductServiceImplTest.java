package com.ecommerce.services.impls;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.Product;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.responses.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith({MockitoExtension.class})
class ProductServiceImplTest {
    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    void createProduct_with_productDTO_null() {
        ProductDTO productDTO = null;

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> productService.createProduct(productDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Product data must not be null", actualThrow.getError().getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(any(Product.class));
    }

    @Test
    void createProduct_with_noName() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("Phone")
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name("")
                .price(1999.0F)
                .quantity(20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(categoryDTO)
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> productService.createProduct(productDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Product name is required", actualThrow.getError().getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(any(Product.class));
    }

    @Test
    void createProduct_with_negative_price() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("Phone")
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name("iphone 16")
                .price(-1999.0F)
                .quantity(20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(categoryDTO)
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> productService.createProduct(productDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Price must be not negative", actualThrow.getError().getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(any(Product.class));
    }

    @Test
    void createProduct_with_negative_quantity() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("Phone")
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name("iphone 16")
                .price(1999.0F)
                .quantity(-20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(categoryDTO)
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> productService.createProduct(productDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Quantity must be not negative", actualThrow.getError().getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(any(Product.class));
    }

    @Test
    void createProduct_with_category_is_null() {
        CategoryDTO categoryDTO = null;

        ProductDTO productDTO = ProductDTO.builder()
                .name("iphone 16")
                .price(1999.0F)
                .quantity(20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(categoryDTO)
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> productService.createProduct(productDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Category name must be not null", actualThrow.getError().getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(any(Product.class));
    }

    @Test
    void createProduct_with_notFound_category() {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("Invalid name")
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name("iphone 16")
                .price(1999.0F)
                .quantity(20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(categoryDTO)
                .build();

        Mockito.when(categoryRepository.existsByName(anyString()))
                .thenReturn(false);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> productService.createProduct(productDTO)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Not found category name", actualThrow.getError().getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(any(Product.class));
    }

    @Test
    void createProduct_success() {
        Category category = Category.builder()
                .id(1)
                .name("Phone")
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name("iphone 16")
                .price(1999.0F)
                .quantity(20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(CategoryDTO.builder()
                        .name("Phone")
                        .build())
                .build();

        Product product = Product.builder()
                .id(1)
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .specification(productDTO.getSpecification())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .availability(productDTO.getAvailability())
                .category(category)
                .build();

        Mockito.when(categoryRepository.existsByName(category.getName()))
                        .thenReturn(true);

        Mockito.when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ProductResponse actual = productService.createProduct(productDTO);

        Assertions.assertEquals(productDTO.getName(), actual.getName());
        Assertions.assertEquals(productDTO.getPrice(), actual.getPrice());
        Assertions.assertEquals(productDTO.getQuantity(), actual.getQuantity());
        Assertions.assertEquals(productDTO.getDescription(), actual.getDescription());
        Assertions.assertEquals(productDTO.getAvailability(), actual.getAvailability());
        Assertions.assertEquals(productDTO.getSpecification(), actual.getSpecification());
        Assertions.assertEquals(productDTO.getCategory().getName(), actual.getCategory().getName());

        Mockito.verify(productRepository, Mockito.times(1))
                .save(any(Product.class));
    }
}