package com.softi.common.kafka.events.orderservice;

import lombok.Data;

@Data
public class OrderCreateEventPosition {

    private String productId;
    private Long quantity;

}
