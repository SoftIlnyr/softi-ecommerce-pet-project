package com.softi.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryRestockRequest {

    private String productId;
    private Long quantity;

}
