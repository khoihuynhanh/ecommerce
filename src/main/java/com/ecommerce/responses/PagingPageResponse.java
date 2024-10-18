package com.ecommerce.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingPageResponse {
    long totalPage;
    long totalItems;
    Object data;
}
