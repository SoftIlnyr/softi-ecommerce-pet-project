package com.softi.orderservice.kafka;

import com.softi.orderservice.dto.OrderDto;

public interface OrderKafkaProducerService {

    void createEventOrderCreated(OrderDto orderDto);
}
