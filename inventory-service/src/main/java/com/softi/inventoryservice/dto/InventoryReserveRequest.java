package com.softi.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryReserveRequest {

    private String productId;
    private Long quantity;
    private String orderId;

}
