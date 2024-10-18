package com.ecommerce.controllers;

import com.ecommerce.dtos.OrderDetailDTO;
import com.ecommerce.responses.OrderDetailResponse;
import com.ecommerce.services.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<OrderDetailResponse> createOrderDetail(
            @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        OrderDetailResponse response = orderDetailService.createOrderDetail(orderDetailDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getAllOrderDetailsByOrder(
            @PathVariable("orderId") int orderId
    ) {
        List<OrderDetailResponse> responses = orderDetailService.getAllOrderDetailsByOrder(orderId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetailById(
            @PathVariable("id") int id
    ) {
        OrderDetailResponse response = orderDetailService.getOrderDetailById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> updateOrderDetail(
            @PathVariable("id") int id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        OrderDetailResponse response = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> deleteOrderDetail(
            @PathVariable("id") int id
    ) {
        OrderDetailResponse response = orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(response);
    }
}
