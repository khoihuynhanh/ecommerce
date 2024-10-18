package com.ecommerce.services.impls;

import com.ecommerce.dtos.*;
import com.ecommerce.entities.OrderDetail;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.repositories.OrderDetailRepository;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class})
class OrderDetailServiceImplTest {
    @InjectMocks
    OrderDetailServiceImpl orderDetailService;

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderDetailRepository orderDetailRepository;

    @Test
    void createOrderDetail_with_order_not_exists() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .build();
        OrderDTO orderDTO = OrderDTO.builder()
                .fullname("full name")
                .phone("123123123")
                .address("address")
                .payment("payment")
                .shippingDate(LocalDate.now().plusDays(1))
                .active(true)
                .account(accountDTO)
                .build();
        ProductDTO productDTO = ProductDTO.builder()
                .name("iphone 16")
                .price(1999.0F)
                .quantity(-20)
                .specification("information iphone 16")
                .description("new version")
                .imageUrl("https://www.apple.com/newsroom/images/2024/09/apple-debuts-iphone-16-pro-and-iphone-16-pro-max/article/Apple-iPhone-16-Pro-hero-geo-240909_inline.jpg.large.jpg")
                .availability("available")
                .category(CategoryDTO.builder()
                        .name("name")
                        .build())
                .build();
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .order(orderDTO)
                .product(productDTO)
                .quantity(3)
                .build();

        Mockito.when(orderRepository.findByFullname("full name"))
                .thenReturn(null);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> orderDetailService.createOrderDetail(orderDetailDTO)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Order is not exists", actualThrow.getError().getMessage());
        Mockito.verify(orderDetailRepository, Mockito.never())
                .save(any(OrderDetail.class));
    }
}