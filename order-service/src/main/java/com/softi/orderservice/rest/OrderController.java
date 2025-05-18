package com.softi.orderservice.rest;

import com.softi.orderservice.dto.CreateOrderDto;
import com.softi.orderservice.dto.OrderDto;
import com.softi.orderservice.dto.OrderPaymentCredentials;
import com.softi.orderservice.mapper.OrderMapper;
import com.softi.orderservice.models.Order;
import com.softi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        OrderDto order = orderService.createOrder(createOrderDto);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/api/orders/{orderId}/pay")
    public ResponseEntity<OrderDto> payOrder(@RequestParam("orderId") String orderId,
                                             OrderPaymentCredentials orderPaymentCredentials) {
        orderService.payOrder(orderId, orderPaymentCredentials);
        return ResponseEntity.ok(new OrderDto());
    }

    @PutMapping("/api/orders/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@RequestParam("orderId") String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new OrderDto());
    }
}
