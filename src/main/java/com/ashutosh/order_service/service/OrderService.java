package com.ashutosh.order_service.service;

import com.ashutosh.order_service.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<OrderDto> placeOrder(OrderDto dto);
    ResponseEntity<OrderDto> trackOrder(Long orderId);
    ResponseEntity<List<OrderDto>> getAllOrders();
    ResponseEntity<OrderDto> updateOrderStatus(Long orderId, String status);
    ResponseEntity<OrderDto> cancelOrder(Long orderId);
}
