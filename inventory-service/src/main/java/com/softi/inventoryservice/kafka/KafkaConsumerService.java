package com.softi.inventoryservice.kafka;

import com.softi.common.kafka.events.productservice.ProductEvent;

public interface KafkaConsumerService {

    void consumeProductEvent(ProductEvent productEvent);
}
