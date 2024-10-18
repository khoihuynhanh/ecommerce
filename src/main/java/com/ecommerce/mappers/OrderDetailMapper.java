package com.ecommerce.mappers;

import com.ecommerce.dtos.OrderDetailDTO;
import com.ecommerce.entities.OrderDetail;
import com.ecommerce.responses.OrderDetailResponse;

public class OrderDetailMapper {
    public static OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO) {
        return OrderDetail.builder()
                .order(OrderMapper.toOrder(orderDetailDTO.getOrder()))
                .product(ProductMapper.toProduct(orderDetailDTO.getProduct()))
                .quantity(orderDetailDTO.getQuantity())
//                .price(orderDetailDTO.getPrice())
//                .totalPrice(orderDetailDTO.getTotalPrice())
                .build();
    }

    public static OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .order(OrderMapper.toOrderResponse(orderDetail.getOrder()))
                .product(ProductMapper.toProductResponse(orderDetail.getProduct()))
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }
}
