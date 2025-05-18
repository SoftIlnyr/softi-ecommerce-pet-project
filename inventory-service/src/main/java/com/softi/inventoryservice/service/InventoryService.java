package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryReserveRequest;

public interface InventoryService {

    InventoryDto getById(Long id);

    InventoryDto reserve(InventoryReserveRequest inventoryDto);
}
