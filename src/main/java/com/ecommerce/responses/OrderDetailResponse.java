package com.ecommerce.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    int id;

    OrderResponse order;

    ProductResponse product;

    int quantity;

    Float price;

    Float totalPrice;
}
