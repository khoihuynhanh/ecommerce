package com.ecommerce.services;

import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.dtos.ProductDTOFilter;
import com.ecommerce.responses.PagingPageResponse;
import com.ecommerce.responses.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductDTO productDTO);

    void generateFakeProducts();

    ProductResponse getProductById(int id);

    PagingPageResponse search(ProductDTOFilter productDTOFilter);

    ProductResponse updateProduct(int id, ProductDTO productDTO);

    ProductResponse deleteProduct(int id);
}
