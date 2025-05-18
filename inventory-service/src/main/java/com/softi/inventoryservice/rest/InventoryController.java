package com.softi.inventoryservice.rest;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryChangeRequest;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.dto.InventoryRestockRequest;
import com.softi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/api/inventory/{id}")
    ResponseEntity<InventoryDto> getInventory(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getById(id));
    }

    @PostMapping("/api/inventory/reserve")
    ResponseEntity<InventoryDto> reserveInventory(@RequestBody InventoryReserveRequest reserveRequest) {
        InventoryDto reservedInventory = inventoryService.reserve(reserveRequest);
        return ResponseEntity.ok(reservedInventory);
    }

    @PostMapping("/api/inventory/release")
    ResponseEntity<InventoryDto> releaseInventory(@RequestBody InventoryChangeRequest releaseRequest) {
        InventoryDto releasedInventory = inventoryService.release(releaseRequest);
        return ResponseEntity.ok(releasedInventory);
    }

    @PostMapping("/api/inventory/cancel")
    ResponseEntity<InventoryDto> cancelInventory(@RequestBody InventoryChangeRequest releaseRequest) {
        InventoryDto cancelledInventory = inventoryService.cancel(releaseRequest);
        return ResponseEntity.ok(cancelledInventory);
    }

    @PostMapping("/api/inventory/restock")
    ResponseEntity<InventoryDto> restockInventory(@RequestBody InventoryRestockRequest restockRequest) {
        InventoryDto restockInventory = inventoryService.restock(restockRequest);
        return ResponseEntity.ok(restockInventory);
    }

}
