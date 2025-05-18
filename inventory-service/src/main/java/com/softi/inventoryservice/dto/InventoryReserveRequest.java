package com.softi.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryReserveRequest {

    private String productId;
    private Integer quantity;
    private String orderId;

}
