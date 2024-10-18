package com.ecommerce.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTOFilter {
    Integer pageIndex = 1;
    Integer pageSize = 10;
    Integer categoryId;
    Double priceFrom = 0d;
    Double priceTo = Double.MAX_VALUE;
    String sortByPrice;
    String name = "";
}
