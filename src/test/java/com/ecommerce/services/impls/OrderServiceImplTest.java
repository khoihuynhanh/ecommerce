package com.ecommerce.services.impls;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.entities.Account;
import com.ecommerce.entities.Order;
import com.ecommerce.entities.Role;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.repositories.AccountRepository;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.responses.OrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class OrderServiceImplTest {
    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    AccountRepository accountRepository;

    @Test
    void createOrder_with_notFound_account() {
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
        Mockito.when(accountRepository.findByEmail("email"))
                .thenReturn(null);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> orderService.createOrder(orderDTO)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Account is not exists", actualThrow.getError().getMessage());
        Mockito.verify(orderRepository, Mockito.never())
                .save(any(Order.class));
    }

    @Test
    void testCreateOrder_ShippingDateInPast() {
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
                .shippingDate(LocalDate.now().minusDays(1))
                .active(true)
                .account(accountDTO)
                .build();

        Mockito.when(accountRepository.findByEmail(accountDTO.getEmail()))
                .thenReturn(new Account());
        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> orderService.createOrder(orderDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Date must be at least today !", actualThrow.getError().getMessage());
        Mockito.verify(orderRepository, Mockito.never())
                .save(any(Order.class));
    }

    @Test
    void createOrder_success() {
        Role role = Role.builder()
                .id(1)
                .name("user")
                .build();

        Account account = Account.builder()
                .id(1)
                .email("email@email.com")
                .username("username")
                .password("password")
                .role(role)
                .build();

        Order order = Order.builder()
                .id(1)
                .fullname("full name")
                .phone("123123123")
                .address("address")
                .payment("payment")
                .shippingDate(LocalDate.now().plusDays(1))
                .active(true)
                .account(account)
                .build();

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
                .shippingDate(LocalDate.now().plusDays(1)) // Ngày giao hàng
                .active(true)
                .account(accountDTO)
                .build();

        Mockito.when(accountRepository.findByEmail(account.getEmail()))
                .thenReturn(account);

        Mockito.when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderResponse actual = orderService.createOrder(orderDTO);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("full name", actual.getFullname());
        Assertions.assertEquals("123123123", actual.getPhone());
        Assertions.assertEquals("address", actual.getAddress());
        Assertions.assertEquals("payment", actual.getPayment());
        Assertions.assertEquals(true, actual.getActive());
        Assertions.assertNotNull(actual.getShippingDate());
        Assertions.assertEquals(order.getShippingDate(), actual.getShippingDate());

        Assertions.assertEquals(account.getId(), actual.getAccount().getId());
        Assertions.assertEquals(account.getEmail(), actual.getAccount().getEmail());
        Assertions.assertEquals(account.getUsername(), actual.getAccount().getUsername());
        Assertions.assertEquals(account.getRole().getName(), actual.getAccount().getRole().getName());

        Mockito.verify(orderRepository, Mockito.times(1))
                .save(any(Order.class));
    }

}