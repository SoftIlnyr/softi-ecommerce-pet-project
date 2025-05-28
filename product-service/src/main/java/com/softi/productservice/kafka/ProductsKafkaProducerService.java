package com.softi.productservice.kafka;

import java.time.LocalDateTime;

public interface ProductsKafkaProducerService {

    void createEventProductCreated(String productId, LocalDateTime creationDateTime);
}
