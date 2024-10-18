package com.ecommerce.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDTO {
    OrderDTO order;

    ProductDTO product;

    int quantity;

//    Float price;
//
//    Float totalPrice;
}
