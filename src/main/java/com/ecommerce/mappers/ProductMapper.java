package com.ecommerce.mappers;

import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.entities.Product;
import com.ecommerce.responses.ProductResponse;

public class ProductMapper {
    public static Product toProduct(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .summary(productDTO.getSummary())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .availability(productDTO.getAvailability())
                .specification(productDTO.getSpecification())
                .category(CategoryMapper.toCategory(productDTO.getCategory()))
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .summary(product.getSummary())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .availability(product.getAvailability())
                .specification(product.getSpecification())
                .category(CategoryMapper.toCategoryResponse(product.getCategory()))
                .build();
    }
}
