package com.ecommerce.controllers;

import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.repositories.AccountRepository;
import com.ecommerce.responses.OrderResponse;
import com.ecommerce.services.AccountService;
import com.ecommerce.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    AccountService accountService;
    OrderService orderService;

    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderDTO orderDTO
    ) {
        OrderResponse response = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable("id") int id
    ) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable("id") int id,
            @RequestBody OrderDTO orderDTO
    ) {
        OrderResponse response = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(
            @PathVariable("id") int id
    ) {
        OrderResponse response = orderService.deleteOrder(id);
        return ResponseEntity.ok(response);
    }
}
