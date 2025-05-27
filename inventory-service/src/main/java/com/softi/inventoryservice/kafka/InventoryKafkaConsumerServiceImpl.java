package com.softi.inventoryservice.kafka;

import com.softi.common.kafka.events.inventoryservice.InventoryReserveEvent;
import com.softi.common.kafka.events.orderservice.OrderCreateEvent;
import com.softi.common.kafka.events.productservice.ProductEvent;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.service.InventoryReserveResult;
import com.softi.inventoryservice.service.InventoryReserveService;
import com.softi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class InventoryKafkaConsumerServiceImpl implements InventoryKafkaConsumerService {

    private final InventoryService inventoryService;
    private final InventoryReserveService inventoryReserveService;

    @KafkaListener(topics = "product_service_events", groupId = "softi-ecommerce-inventory-service")
    @Override
    public void consumeProductEvent(ProductEvent productEvent) {
        log.info("Received product event: {}", productEvent);
        inventoryService.createInventory(productEvent.getProductId(), productEvent.getCreatedAt());
    }

    @KafkaListener(topics = "order_create", groupId = "softi-ecommerce-inventory-service")
    @Override
    public void consumeOrderCreatedEvent(OrderCreateEvent orderCreateEvent) {
        log.info("Received order created event: {}", orderCreateEvent);
        List<InventoryReserveRequest> inventoryRequests = new ArrayList<>();
        for (var orderPosition : orderCreateEvent.getPositions()) {
            InventoryReserveRequest inventoryReserveRequest = new InventoryReserveRequest();
            inventoryReserveRequest.setOrderId(orderCreateEvent.getOrderId());
            inventoryReserveRequest.setProductId(orderPosition.getProductId());
            inventoryReserveRequest.setQuantity(orderPosition.getQuantity());
            inventoryRequests.add(inventoryReserveRequest);
        }
        ;
        InventoryReserveResult reserveResult = inventoryReserveService.reserveAll(inventoryRequests);
    }

    @KafkaListener(topics = "order_pay", groupId = "softi-ecommerce-inventory-service")
    public void consumeOrderPayedEvent(String orderId) {
        log.info("Received order payed event: {}", orderId);
        inventoryService.releaseOrder(orderId);
    }

}
