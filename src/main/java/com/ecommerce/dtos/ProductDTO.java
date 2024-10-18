package com.ecommerce.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    String name;
    Float price;
    int quantity;
    String summary;
    String description;
    String imageUrl;
    String availability;
    String specification;
    CategoryDTO category;
}
