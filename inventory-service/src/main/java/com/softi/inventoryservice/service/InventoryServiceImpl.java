package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.entity.InventoryEntity;
import com.softi.inventoryservice.exception.InsufficientStockException;
import com.softi.inventoryservice.mapper.InventoryMapper;
import com.softi.inventoryservice.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryDto getById(Long id) {
        InventoryEntity inventoryEntity = inventoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Inventory with id %s not found".formatted(id)));
        return inventoryMapper.toDto(inventoryEntity);
    }

    @Override
    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class, InsufficientStockException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public InventoryDto reserve(InventoryReserveRequest reserveRequest) {
        String productId = reserveRequest.getProductId();
        InventoryEntity inventoryEntity = inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new EntityNotFoundException("Inventory with id %s not found".formatted(productId)));

        Integer quantity = reserveRequest.getQuantity();

        // Проверяем доступное количество
        if (inventoryEntity.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock for product with id " + productId);
        }

        inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() - quantity);
        inventoryEntity.setReservedQuantity(inventoryEntity.getReservedQuantity() + quantity);
        inventoryRepository.save(inventoryEntity);

        return null;
    }
}
