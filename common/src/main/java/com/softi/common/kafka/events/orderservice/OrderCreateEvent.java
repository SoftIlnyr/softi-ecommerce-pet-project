package com.softi.common.kafka.events.orderservice;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateEvent {

    private String orderId;
    private List<OrderCreateEventPosition> positions;

}
