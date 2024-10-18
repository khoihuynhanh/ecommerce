package com.ecommerce.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    int id;

    String fullname;

    String phone;

    String address;

    String payment;

    Date orderDate;

    LocalDate shippingDate;

    Boolean active;

    AccountResponse account;
}
