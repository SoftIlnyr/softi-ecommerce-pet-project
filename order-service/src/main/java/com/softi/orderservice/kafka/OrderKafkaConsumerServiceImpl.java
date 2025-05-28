package com.softi.orderservice.kafka;

import com.softi.common.kafka.events.inventoryservice.InventoryReserveEvent;
import com.softi.common.kafka.events.inventoryservice.InventoryReserveStatus;
import com.softi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class OrderKafkaConsumerServiceImpl implements OrderKafkaConsumerService {

    private final OrderService orderService;

    @KafkaListener(topics = "inventory_reserve_events", groupId = "softi-ecommerce-order-service")
    @Override
    public void consumeProductEvent(InventoryReserveEvent reserveEvent) {
        log.info("Received inventory reserve event: {}", reserveEvent);
        if (reserveEvent == null) {
            log.info("Received null event for reserve event");
            return;
        }
        if (reserveEvent.getStatus() == InventoryReserveStatus.CANCEL) {
            orderService.cancelOrder(reserveEvent.getOrderId());
            log.info("Inventory reserve cancelled");
        }
    }

}
