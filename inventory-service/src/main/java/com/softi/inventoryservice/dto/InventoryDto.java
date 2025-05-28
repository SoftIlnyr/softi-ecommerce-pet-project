package com.softi.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryDto {

    private long id;
    private String productId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
}
