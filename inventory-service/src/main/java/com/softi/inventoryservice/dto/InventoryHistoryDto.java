package com.softi.inventoryservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryHistoryDto {

    private long id;
    private String productId;
    private String changeType;
    private Integer quantity;
    private String orderId;
    private LocalDateTime creationDateTime;

}
