package com.ecommerce.services.impls;

import com.ecommerce.repositories.criterias.ProductRepositoryCriteria;
import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.dtos.ProductDTOFilter;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.Product;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.mappers.ProductMapper;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.responses.PagingPageResponse;
import com.ecommerce.responses.ProductResponse;
import com.ecommerce.services.ProductService;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ProductRepositoryCriteria productRepositoryCriteria;

    @Override
    public ProductResponse createProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            throw EcommerceException.badRequestException("Product data must not be null");
        }
        if (productDTO.getName().isEmpty()) {
            throw EcommerceException.badRequestException("Product name is required");
        }
        if (productDTO.getPrice() < 0) {
            throw EcommerceException.badRequestException("Price must be not negative");
        }
        if (productDTO.getQuantity() < 0) {
            throw EcommerceException.badRequestException("Quantity must be not negative");
        }
        if (productDTO.getCategory() == null) {
            throw EcommerceException.badRequestException("Category name must be not null");
        }
        if (!categoryRepository.existsByName(productDTO.getCategory().getName())) {
            throw EcommerceException.notFoundException("Not found category name");
        }

        Category category = categoryRepository.findByName(productDTO.getCategory().getName().toLowerCase());

        Product product = ProductMapper.toProduct(productDTO);
        product.setCategory(category);
        product = productRepository.save(product);
        return ProductMapper.toProductResponse(product);
    }

    @Override
    public void generateFakeProducts() {
        Faker faker = new Faker();

        List<Integer> categoryIds = categoryRepository.findAll()
                .stream()
                .map(Category::getId)
                .toList();

        for (int i = 0; i < 50; i++) {
            String productName = faker.commerce().productName();

            if (productRepository.existsByName(productName)) {
                continue;
            }

            Integer randomCategoryId = categoryIds.get(faker.random().nextInt(categoryIds.size()));

            Category randomCategory = categoryRepository.findById(randomCategoryId)
                    .orElseThrow(() -> EcommerceException.notFoundException("not found id"));

            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90_000_000))
                    .quantity(faker.number().numberBetween(1, 20))
                    .summary(faker.lorem().sentence())
                    .description(faker.lorem().sentence())
                    .imageUrl(faker.internet().image())
                    .availability("In stock")
                    .specification(faker.lorem().sentence())
                    .category(CategoryDTO.builder()
                            .name(randomCategory.getName())
                            .build())
                    .build();

            createProduct(productDTO);
        }
    }

    @Override
    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found id"));
        return ProductMapper.toProductResponse(product);
    }

    @Override
    public PagingPageResponse search(ProductDTOFilter productDTOFilter) {
        return productRepositoryCriteria.search(productDTOFilter);
    }

    @Override
    public ProductResponse updateProduct(int id, ProductDTO productDTO) {
        Category existingCategory = categoryRepository.findByName(productDTO.getCategory().getName().toLowerCase());
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found id"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setSummary(productDTO.getSummary());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setAvailability(productDTO.getAvailability());
        existingProduct.setSpecification(productDTO.getSpecification());
        existingProduct.setCategory(existingCategory);

        Product updatedProduct = productRepository.save(existingProduct);

        return ProductMapper.toProductResponse(updatedProduct);
    }

    @Override
    public ProductResponse deleteProduct(int id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found id"));
        productRepository.delete(existingProduct);
        return ProductMapper.toProductResponse(existingProduct);
    }
}
