package com.ecommerce.services.impls;

import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.entities.Account;
import com.ecommerce.entities.Order;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.mappers.OrderMapper;
import com.ecommerce.repositories.AccountRepository;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.responses.OrderResponse;
import com.ecommerce.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    AccountRepository accountRepository;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        Account existingAccount = accountRepository.findByEmail(orderDTO.getAccount().getEmail());
        if (existingAccount == null) {
            throw EcommerceException.notFoundException("Account is not exists");
        }
        Order order = OrderMapper.toOrder(orderDTO);
        order.setOrderDate(new Date());
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw EcommerceException.badRequestException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setAccount(existingAccount);
        order = orderRepository.save(order);
        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found id"));
        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrder(int id, OrderDTO orderDTO) {
        Account existingAccount = accountRepository.findByEmail(orderDTO.getAccount().getEmail());
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found order"));
        existingOrder.setFullname(orderDTO.getFullname());
        existingOrder.setPhone(orderDTO.getPhone());
        existingOrder.setAddress(orderDTO.getAddress());
        existingOrder.setPayment(orderDTO.getPayment());
        existingOrder.setOrderDate(new Date());
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw EcommerceException.badRequestException("Date must be at least today !");
        }
        existingOrder.setShippingDate(shippingDate);
        existingOrder.setActive(existingOrder.getActive());
        Order updateOrder = orderRepository.save(existingOrder);
        existingOrder.setAccount(existingAccount);
        return OrderMapper.toOrderResponse(updateOrder);
    }

    @Override
    public OrderResponse deleteOrder(int id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found order"));
        existingOrder.setActive(false);
        Order deleteOrder = orderRepository.save(existingOrder);
        return OrderMapper.toOrderResponse(deleteOrder);
    }
}
