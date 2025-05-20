package com.softi.productservice.kafka;

import com.softi.common.kafka.events.productservice.ProductEvent;
import com.softi.common.kafka.events.productservice.ProductEventType;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductsKafkaProducerServiceImpl implements ProductsKafkaProducerService {

    @Autowired
    private KafkaTemplate<String, ProductEvent> productEventKafkaTemplate;

    @Autowired
    @Qualifier("productServiceEventsTopic")
    private NewTopic productServiceEventsTopic;

    @Override
    @Async
    @Retryable(
            value = { KafkaException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void createEventProductCreated(String productId, LocalDateTime creationDateTime) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductId(productId);
        productEvent.setProductEventType(ProductEventType.CREATED);
        productEvent.setCreatedAt(creationDateTime);
        productEventKafkaTemplate.send(productServiceEventsTopic.name(), productEvent);
    }
}
