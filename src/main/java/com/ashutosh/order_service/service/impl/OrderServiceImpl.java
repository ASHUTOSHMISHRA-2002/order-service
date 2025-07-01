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
    public ResponseEntity<OrderDto> updateOrder(OrderDto dto) {
        Order order = orderRepository.findById(dto.getOrderId()).get();

        order.setCustomerId(dto.getCustomerId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(dto.getStatus());

        Order updated = orderRepository.save(order);

        OrderDto response =  new OrderDto(
            updated.getOrderId(),
            updated.getCustomerId(),
            updated.getRestaurantId(),
            updated.getStatus(),
            updated.getTotalAmount()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    
}
