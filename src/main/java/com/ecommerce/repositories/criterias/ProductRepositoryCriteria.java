package com.ecommerce.repositories.criterias;

import com.ecommerce.dtos.ProductDTOFilter;
import com.ecommerce.entities.Product;
import com.ecommerce.mappers.ProductMapper;
import com.ecommerce.responses.PagingPageResponse;
import com.ecommerce.responses.ProductResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRepositoryCriteria {
    EntityManager em;

    public PagingPageResponse search(ProductDTOFilter productDTOFilter) {
        StringBuilder sql = new StringBuilder("select p from Product p where 1=1");
        Map<String, Object> params = new HashMap<>();

        if (productDTOFilter.getCategoryId() != null) {
            sql.append(" and p.category.id = :categoryId ");
            params.put("categoryId", productDTOFilter.getCategoryId());
        }

        if (productDTOFilter.getPriceFrom() != null && productDTOFilter.getPriceTo() != null) {
            sql.append(" and p.price between :priceFrom and :priceTo");
            params.put("priceFrom", productDTOFilter.getPriceFrom());
            params.put("priceTo", productDTOFilter.getPriceTo());
        }

        if (productDTOFilter.getSortByPrice() != null) {
            sql.append(" order by p.price ").append(productDTOFilter.getSortByPrice());
        }

        if (productDTOFilter.getName() != null && !productDTOFilter.getName().isEmpty()) {
            sql.append(" and LOWER(p.name) like LOWER(:name) ");
            params.put("name", "%" + productDTOFilter.getName().toLowerCase() + "%");
        }

        Query countQuery = em.createQuery(sql.toString()
                .replace("select p", "select count(p.id)"));

        TypedQuery<Product> productTypedQuery = em.createQuery(sql.toString(), Product.class);

        params.forEach((k, v) -> {
            productTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        Integer pageIndex = productDTOFilter.getPageIndex();
        Integer pageSize = productDTOFilter.getPageSize();
        long totalProduct = (long) countQuery.getSingleResult();
        long totalPage = (totalProduct + pageSize - 1) / pageSize;

        if (totalProduct % pageSize != 0) {
            pageSize++;
        }

        // paging
        productTypedQuery.setFirstResult((pageIndex - 1) * pageSize);
        productTypedQuery.setMaxResults(pageSize);
        List<Product> productList = productTypedQuery.getResultList();

        List<ProductResponse> productResponseList = productList.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());

        return PagingPageResponse.builder()
                .totalPage(totalPage)
                .totalItems(totalProduct)
                .data(productResponseList)
                .build();
    }
}
