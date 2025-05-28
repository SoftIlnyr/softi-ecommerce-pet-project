package com.softi.inventoryservice.kafka;

import com.softi.inventoryservice.service.InventoryReserveResult;

public interface InventoryKafkaProducerService {
    void createInventoryReservedEvent(InventoryReserveResult reserveResult);
}
