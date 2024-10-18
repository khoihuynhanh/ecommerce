package com.ecommerce.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    int id;
    String name;
    Float price;
    int quantity;
    String summary;
    String description;
    String imageUrl;
    String availability;
    String specification;
    CategoryResponse category;
}
