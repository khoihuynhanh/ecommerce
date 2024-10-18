package com.ecommerce.services.impls;

import com.ecommerce.dtos.OrderDetailDTO;
import com.ecommerce.entities.Order;
import com.ecommerce.entities.OrderDetail;
import com.ecommerce.entities.Product;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.mappers.OrderDetailMapper;
import com.ecommerce.repositories.OrderDetailRepository;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.responses.OrderDetailResponse;
import com.ecommerce.services.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order existingOrder = orderRepository.findByFullname(orderDetailDTO.getOrder().getFullname());
        if (existingOrder == null) {
            throw EcommerceException.notFoundException("Order is not exists");
        }

        Product existingProduct = productRepository.findByName(orderDetailDTO.getProduct().getName());
        if (existingProduct == null) {
            throw EcommerceException.notFoundException("Product is not exist");
        }

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);

        int quantityProduct = existingProduct.getQuantity();
        int requestedQuantity = orderDetailDTO.getQuantity();

        if (requestedQuantity > quantityProduct) {
            throw new IllegalArgumentException("Số lượng sản phẩm trong kho không đủ");
        }

        existingProduct.setQuantity(quantityProduct - requestedQuantity);
        productRepository.save(existingProduct);

        orderDetail.setQuantity(requestedQuantity);

        Float price = existingProduct.getPrice();
        orderDetail.setPrice(price);

        Float totalPrice = price * requestedQuantity;
        orderDetail.setTotalPrice(totalPrice);

        orderDetail = orderDetailRepository.save(orderDetail);
        return OrderDetailMapper.toOrderDetailResponse(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailsByOrder(int orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        return orderDetailList.stream()
                .map(OrderDetailMapper::toOrderDetailResponse)
                .toList();
    }

    @Override
    public OrderDetailResponse getOrderDetailById(int id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found id"));
        return OrderDetailMapper.toOrderDetailResponse(orderDetail);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(int id, OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("OrderDetail not found"));

        Order existingOrder = orderRepository.findByFullname(orderDetailDTO.getOrder().getFullname());
        if (existingOrder == null) {
            throw EcommerceException.notFoundException("Order not found");
        }

        Product existingProduct = productRepository.findByName(orderDetailDTO.getProduct().getName());
        if (existingProduct == null) {
            throw EcommerceException.notFoundException("Product not found");
        }

        int oldQuantity = orderDetail.getQuantity();
        int newQuantity = orderDetailDTO.getQuantity();

        orderDetail.setOrder(existingOrder);
        orderDetail.setQuantity(newQuantity);
        orderDetail.setProduct(existingProduct);

        int currentProductQuantity = existingProduct.getQuantity();

        existingProduct.setQuantity(currentProductQuantity - (newQuantity - oldQuantity));

        Float price = existingProduct.getPrice();
        orderDetail.setPrice(price);
        Float totalPrice = price * newQuantity;
        orderDetail.setTotalPrice(totalPrice);

        orderDetailRepository.save(orderDetail);
        productRepository.save(existingProduct);

        return OrderDetailMapper.toOrderDetailResponse(orderDetail);
    }


    @Override
    public OrderDetailResponse deleteOrderDetail(int id) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("not found order detail"));
        orderDetailRepository.delete(existingOrderDetail);
        return OrderDetailMapper.toOrderDetailResponse(existingOrderDetail);
    }


}
