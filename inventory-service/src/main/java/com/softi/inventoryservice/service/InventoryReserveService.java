package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryReserveRequest;

import java.util.List;

public interface InventoryReserveService {

    InventoryReserveResult reserveAll(List<InventoryReserveRequest> reserveRequests);
}
