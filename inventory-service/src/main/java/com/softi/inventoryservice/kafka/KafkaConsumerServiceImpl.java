package com.softi.inventoryservice.kafka;

import com.softi.common.kafka.events.productservice.ProductEvent;
import com.softi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "softi", groupId = "product_service_events")
    @Override
    public void consumeProductEvent(ProductEvent productEvent) {
        log.info("Received product event: {}", productEvent);
        inventoryService.createInventory(productEvent.getProductId(), productEvent.getCreatedAt());
    }
}
