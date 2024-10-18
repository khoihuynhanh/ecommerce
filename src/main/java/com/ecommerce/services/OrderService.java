package com.ecommerce.services;

import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.responses.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderDTO orderDTO);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(int id);

    OrderResponse updateOrder(int id, OrderDTO orderDTO);

    OrderResponse deleteOrder(int id);
}
