package com.softi.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryChangeRequest {

    private String productId;
    private String orderId;

}
