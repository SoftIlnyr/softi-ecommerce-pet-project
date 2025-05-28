package com.softi.orderservice.kafka;

import com.softi.common.kafka.events.orderservice.OrderCreateEvent;
import com.softi.orderservice.dto.OrderDto;
import com.softi.orderservice.mapper.OrderMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OrderKafkaProducerServiceImpl implements OrderKafkaProducerService {

    @Autowired
    private KafkaTemplate<String, OrderCreateEvent> orderCreateKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> orderPayKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> orderCancelKafkaTemplate;

    @Autowired
    @Qualifier("orderCreateTopic")
    private NewTopic orderCreateTopic;

    @Autowired
    @Qualifier("orderPayTopic")
    private NewTopic orderPayTopic;

    @Autowired
    @Qualifier("orderCancelTopic")
    private NewTopic orderCancelTopic;

    @Autowired
    private OrderMapper orderMapper;

    @Async
    @Retryable(
            value = {KafkaException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Override
    public void createEventOrderCreated(OrderDto orderDto) {
        OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
        orderCreateEvent.setOrderId(orderDto.getId());
        orderCreateEvent.setPositions(orderMapper.toEventDtoList(orderDto.getItems()));

        orderCreateKafkaTemplate.send(orderCreateTopic.name(), orderCreateEvent);
    }

    @Async
    @Retryable(
            value = {KafkaException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Override
    public void createEventOrderPayed(String orderId) {
        orderPayKafkaTemplate.send(orderPayTopic.name(), orderId);
    }

    @Async
    @Retryable(
            value = {KafkaException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Override
    public void createEventOrderCancelled(String orderId) {
        orderCancelKafkaTemplate.send(orderCancelTopic.name(), orderId);
    }
}
