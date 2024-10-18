package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tbl_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String fullname;

    String phone;

    String address;

    String payment;

    Date orderDate;

    LocalDate shippingDate;

    Boolean active;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

}
