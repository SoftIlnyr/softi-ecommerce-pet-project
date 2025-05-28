package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryChangeRequest;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.dto.InventoryRestockRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryService {

    InventoryDto getById(Long id);

    InventoryDto reserve(InventoryReserveRequest reserveRequest);

    InventoryDto release(InventoryChangeRequest releaseRequest);

    List<InventoryDto> releaseOrder(String orderId);

    InventoryDto cancel(InventoryChangeRequest releaseRequest);

    InventoryDto restock(InventoryRestockRequest restockRequest);

    InventoryDto createInventory(String productId, LocalDateTime createdAt);

    List<InventoryDto> getAll();
}
