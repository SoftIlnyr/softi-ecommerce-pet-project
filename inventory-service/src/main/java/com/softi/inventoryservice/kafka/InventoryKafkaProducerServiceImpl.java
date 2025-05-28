package com.softi.inventoryservice.kafka;

import com.softi.common.kafka.events.inventoryservice.InventoryReserveEvent;
import com.softi.common.kafka.events.inventoryservice.InventoryReserveStatus;
import com.softi.inventoryservice.service.InventoryReserveResult;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InventoryKafkaProducerServiceImpl implements InventoryKafkaProducerService {

    @Autowired
    private KafkaTemplate<String, InventoryReserveEvent> inventoryReserveKafkaTemplate;

    @Autowired
    @Qualifier("inventoryReserveStatusTopic")
    private NewTopic inventoryReserveStatusTopic;

    @Async
    @Retryable(
            value = {KafkaException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Override
    public void createInventoryReservedEvent(InventoryReserveResult reserveResult) {
        InventoryReserveEvent reserveEvent = new InventoryReserveEvent();
        reserveEvent.setOrderId(reserveResult.getOrderId());
        reserveEvent.setStatus(resolveStatus(reserveResult));
        reserveEvent.setReservedProductIds(reserveResult.getReservedProductIds());
        reserveEvent.setCancelledProductIds(reserveResult.getCancelledProductIds());

        inventoryReserveKafkaTemplate.send(inventoryReserveStatusTopic.name(), reserveEvent);
    }

    private InventoryReserveStatus resolveStatus(InventoryReserveResult result) {
        if (!result.getReservedProductIds().isEmpty()) {
            return result.getCancelledProductIds().isEmpty() ? InventoryReserveStatus.FULL : InventoryReserveStatus.PARTIAL;
        }
        return InventoryReserveStatus.CANCEL;
    }
}
