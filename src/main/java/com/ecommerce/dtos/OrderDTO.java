package com.ecommerce.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    String fullname;

    String phone;

    String address;

    String payment;

//    Date orderDate;

    LocalDate shippingDate;

    Boolean active;

    AccountDTO account;
}
