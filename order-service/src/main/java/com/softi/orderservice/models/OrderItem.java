package com.softi.orderservice.models;

import lombok.Data;

@Data
public class OrderItem {

    private String productId;
    private Long quantity;

}
