package com.ecommerce.services;

import com.ecommerce.dtos.OrderDetailDTO;
import com.ecommerce.responses.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO);

    List<OrderDetailResponse> getAllOrderDetailsByOrder(int orderId);

    OrderDetailResponse getOrderDetailById(int id);

    OrderDetailResponse updateOrderDetail(int id, OrderDetailDTO orderDetailDTO);

    OrderDetailResponse deleteOrderDetail(int id);
}
