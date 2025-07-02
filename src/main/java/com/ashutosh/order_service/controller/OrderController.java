package com.ashutosh.order_service.controller;

import com.ashutosh.order_service.dto.OrderDto;
import com.ashutosh.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/placeorders")
	public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderDto orderDto) {
		ResponseEntity<OrderDto> createdOrder = orderService.placeOrder(orderDto);
		return createdOrder;
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> trackOrder(@PathVariable("orderId") Long orderId) {
		ResponseEntity<OrderDto> orderDto = orderService.trackOrder(orderId);
		return orderDto;
	}

	@GetMapping("/getallorders")
	public ResponseEntity<List<OrderDto>> getAllOrders() {
		ResponseEntity<List<OrderDto>> orders = orderService.getAllOrders();
		return orders;
	}

	@PutMapping("/{orderId}/status")
	public ResponseEntity<OrderDto> updateOrderStatus(
			@PathVariable("orderId") Long orderId,
			@RequestParam("status") String status) {
		ResponseEntity<OrderDto> updatedOrder = orderService.updateOrderStatus(orderId, status);
		return updatedOrder;
	}


	//Optional
	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<OrderDto> cancelOrder(@PathVariable("orderId") Long OrderId) {
		ResponseEntity<OrderDto> orderDto = orderService.cancelOrder(OrderId);
		return orderDto;
	}
}
