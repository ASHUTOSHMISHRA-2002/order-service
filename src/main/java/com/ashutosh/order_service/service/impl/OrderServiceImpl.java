package com.ashutosh.order_service.service.impl;

import com.ashutosh.order_service.dto.OrderDto;
import com.ashutosh.order_service.entity.Order;
import com.ashutosh.order_service.entity.OrderStatus;
import com.ashutosh.order_service.exception.ResourceNotFoundException;
import com.ashutosh.order_service.repository.OrderRepository;
import com.ashutosh.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public ResponseEntity<OrderDto> placeOrder(OrderDto dto) {
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(OrderStatus.ACCEPTED); // or PENDING as default

        Order savedOrder = orderRepository.save(order);

        OrderDto response = new OrderDto(
            savedOrder.getOrderId(),
            savedOrder.getCustomerId(),
            savedOrder.getRestaurantId(),
            savedOrder.getStatus(),
            savedOrder.getTotalAmount()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OrderDto> trackOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        OrderDto response = new OrderDto(
            order.getOrderId(),
            order.getCustomerId(),
            order.getRestaurantId(),
            order.getStatus(),
            order.getTotalAmount()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        List<OrderDto> responseList = orders.stream()
            .map(order -> new OrderDto(
                order.getOrderId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getTotalAmount()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    
    @Override
    public ResponseEntity<OrderDto> updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);

        OrderDto response = new OrderDto(
                updated.getOrderId(),
                updated.getCustomerId(),
                updated.getRestaurantId(),
                updated.getStatus(),
                updated.getTotalAmount()
        );

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<OrderDto> cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Order cannot be cancelled at this stage: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelled = orderRepository.save(order);

        OrderDto response = new OrderDto(
                cancelled.getOrderId(),
                cancelled.getCustomerId(),
                cancelled.getRestaurantId(),
                cancelled.getStatus(),
                cancelled.getTotalAmount()
        );

        return ResponseEntity.ok(response);
    }


}
