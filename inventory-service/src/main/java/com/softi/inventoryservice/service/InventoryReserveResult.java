package com.softi.inventoryservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InventoryReserveResult {

    private String orderId;
    private List<String> reservedProductIds;
    private List<String> cancelledProductIds;

}
