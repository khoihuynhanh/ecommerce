package com.ecommerce.controllers;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.dtos.ProductDTOFilter;
import com.ecommerce.responses.PagingPageResponse;
import com.ecommerce.responses.ProductResponse;
import com.ecommerce.services.ProductService;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        productService.generateFakeProducts();
        return ResponseEntity.ok("Generated fake products successfully.");
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductDTO productDTO
    ) {
        ProductResponse create = productService.createProduct(productDTO);
        return ResponseEntity.ok(create);
    }

    @GetMapping("/search")
    public ResponseEntity<PagingPageResponse> searchProduct(
            @ModelAttribute ProductDTOFilter productDTOFilter
    ) {
        PagingPageResponse result = productService.search(productDTOFilter);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable("id") int id
    ) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") int id,
            @RequestBody ProductDTO productDTO
    ) {
        ProductResponse result = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable("id") int id
    ) {
        ProductResponse result = productService.deleteProduct(id);
        return ResponseEntity.ok(result);
    }
}
