package com.softi.orderservice.kafka;

import com.softi.common.kafka.events.inventoryservice.InventoryReserveEvent;
import org.springframework.kafka.annotation.KafkaListener;

public interface OrderKafkaConsumerService {
    @KafkaListener(topics = "inventory_reserve_events", groupId = "softi-ecommerce-order-service")
    void consumeProductEvent(InventoryReserveEvent reserveEvent);
}
