package com.softi.inventoryservice.repository;

import com.softi.inventoryservice.entity.InventoryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryHistoryRepository extends JpaRepository<InventoryHistoryEntity, Long> {

}
