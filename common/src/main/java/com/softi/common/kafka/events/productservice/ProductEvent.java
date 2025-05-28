package com.softi.common.kafka.events.productservice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductEvent {

    private String productId;
    private ProductEventType productEventType;
    private LocalDateTime createdAt;

}
