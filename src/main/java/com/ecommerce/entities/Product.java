package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    Float price;
    int quantity;
    @Lob
    @Column(columnDefinition = "CLOB")
    String summary;
    @Lob
    @Column(columnDefinition = "CLOB")
    String description;
    String imageUrl;
    String availability;
    String specification;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    Category category;
}
