package com.ecommerce.mappers;

import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.entities.Order;
import com.ecommerce.responses.OrderResponse;

public class OrderMapper {
    public static Order toOrder(OrderDTO orderDTO) {
        return Order.builder()
                .fullname(orderDTO.getFullname())
                .phone(orderDTO.getPhone())
                .address(orderDTO.getAddress())
                .payment(orderDTO.getPayment())
//                .orderDate(orderDTO.getOrderDate())
                .shippingDate(orderDTO.getShippingDate())
                .active(orderDTO.getActive())
                .account(AccountMapper.toAccount(orderDTO.getAccount()))
                .build();
    }

    public static OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .fullname(order.getFullname())
                .phone(order.getPhone())
                .address(order.getAddress())
                .payment(order.getPayment())
                .orderDate(order.getOrderDate())
                .shippingDate(order.getShippingDate())
                .active(order.getActive())
                .account(AccountMapper.toAccountResponse(order.getAccount()))
                .build();
    }
}
