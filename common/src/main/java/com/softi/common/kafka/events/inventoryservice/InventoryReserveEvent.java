package com.softi.common.kafka.events.inventoryservice;

import lombok.Data;

import java.util.List;

@Data
public class InventoryReserveEvent {

    private String orderId;
    private InventoryReserveStatus status;
    private List<String> reservedProductIds;
    private List<String> cancelledProductIds;

}
