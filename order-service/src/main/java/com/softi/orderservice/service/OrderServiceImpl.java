package com.softi.orderservice.service;

import com.softi.common.exception.EntityNotFoundException;
import com.softi.orderservice.dto.CreateOrderDto;
import com.softi.orderservice.dto.OrderDto;
import com.softi.orderservice.dto.OrderPaymentCredentials;
import com.softi.orderservice.dto.OrderStatus;
import com.softi.orderservice.kafka.OrderKafkaProducerService;
import com.softi.orderservice.mapper.OrderMapper;
import com.softi.orderservice.models.Order;
import com.softi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderKafkaProducerService orderKafkaProducerService;

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderDtoList(orders);
    }

    @Override
    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        Order order = orderMapper.toOrder(createOrderDto);
        //todo order.setCustomerId(customerId);
        order.setStatus(OrderStatus.OPENED.name());
        Order savedOrder = orderRepository.save(order);

        OrderDto orderDto = orderMapper.toOrderDto(savedOrder);
        orderKafkaProducerService.createEventOrderCreated(orderDto);
        return orderDto;
    }

    @Override
    @Transactional
    public void payOrder(String orderId, OrderPaymentCredentials orderPaymentCredentials) {
        Order order = getOrder(orderId);
        boolean paymentProcessed = Math.random() > 0.2;
        if (paymentProcessed) {
            order.setStatus(OrderStatus.RELEASED.name());
            Order savedOrder = orderRepository.save(order);
            orderKafkaProducerService.createEventOrderPayed(order.getId());
        }
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        Order order = getOrder(orderId);
        processCancel(order);
        orderKafkaProducerService.createEventOrderCancelled(order.getId());
    }

    private void processCancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED.name());
        orderRepository.save(order);
    }

    private Order getOrder(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Order with id %s not found".formatted(orderId)));
    }

}
