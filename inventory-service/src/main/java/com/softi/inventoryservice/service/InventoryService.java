package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryChangeRequest;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.dto.InventoryRestockRequest;

public interface InventoryService {

    InventoryDto getById(Long id);

    InventoryDto reserve(InventoryReserveRequest reserveRequest);

    InventoryDto release(InventoryChangeRequest releaseRequest);

    InventoryDto cancel(InventoryChangeRequest releaseRequest);

    InventoryDto restock(InventoryRestockRequest restockRequest);
}
