package com.softi.inventoryservice.kafka;

import com.softi.common.kafka.events.inventoryservice.InventoryReserveEvent;
import com.softi.common.kafka.events.orderservice.OrderCreateEvent;
import com.softi.common.kafka.events.productservice.ProductEvent;

public interface InventoryKafkaConsumerService {

    void consumeProductEvent(ProductEvent productEvent);

    void consumeOrderCreatedEvent(OrderCreateEvent orderCreateEvent);

    void sendInventoryStatusEvent(InventoryReserveEvent inventoryReserveEvent);
}
