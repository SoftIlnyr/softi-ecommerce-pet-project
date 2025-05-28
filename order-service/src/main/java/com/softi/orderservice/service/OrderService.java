package com.softi.orderservice.service;

import com.softi.orderservice.dto.CreateOrderDto;
import com.softi.orderservice.dto.OrderDto;
import com.softi.orderservice.dto.OrderPaymentCredentials;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAllOrders();

    OrderDto createOrder(CreateOrderDto createOrderDto);

    void payOrder(String orderId, OrderPaymentCredentials orderPaymentCredentials);

    void cancelOrder(String orderId);
}
