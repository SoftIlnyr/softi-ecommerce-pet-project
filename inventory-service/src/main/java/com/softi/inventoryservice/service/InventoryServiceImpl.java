package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryChangeRequest;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.dto.InventoryRestockRequest;
import com.softi.inventoryservice.entity.InventoryChangeType;
import com.softi.inventoryservice.entity.InventoryEntity;
import com.softi.inventoryservice.entity.InventoryHistoryEntity;
import com.softi.inventoryservice.entity.ReserveRequestEntity;
import com.softi.inventoryservice.entity.ReserveRequestStatus;
import com.softi.inventoryservice.exception.InsufficientStockException;
import com.softi.inventoryservice.mapper.InventoryMapper;
import com.softi.inventoryservice.repository.InventoryHistoryRepository;
import com.softi.inventoryservice.repository.InventoryRepository;
import com.softi.inventoryservice.repository.ReserveRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final ReserveRequestRepository reserveRequestRepository;

    @Override
    public InventoryDto getById(Long id) {
        InventoryEntity inventoryEntity = inventoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Inventory with id %s not found".formatted(id)));
        return inventoryMapper.toDto(inventoryEntity);
    }

    @Override
    public List<InventoryDto> getAll() {
        List<InventoryEntity> result = inventoryRepository.findAll();
        return inventoryMapper.toDto(result);
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
        InventoryEntity inventoryEntity = getInventory(productId);

        Long quantity = reserveRequest.getQuantity();

        // Проверяем доступное количество
        if (inventoryEntity.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock for product with id " + productId);
        }

        inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() - quantity);
        inventoryEntity.setReservedQuantity(inventoryEntity.getReservedQuantity() + quantity);
        inventoryEntity.setLastUpdateDateTime(LocalDateTime.now());
        InventoryEntity save = inventoryRepository.save(inventoryEntity);

        ReserveRequestEntity reserveRequestEntity = new ReserveRequestEntity();
        reserveRequestEntity.setProductId(productId);
        reserveRequestEntity.setOrderId(reserveRequest.getOrderId());
        reserveRequestEntity.setQuantity(reserveRequest.getQuantity());
        reserveRequestEntity.setStatus(ReserveRequestStatus.OPEN.name());

        reserveRequestRepository.save(reserveRequestEntity);

        return inventoryMapper.toDto(save);
    }

    @Override
    @Transactional
    public InventoryDto release(InventoryChangeRequest releaseRequest) {
        String productId = releaseRequest.getProductId();
        String orderId = releaseRequest.getOrderId();
        InventoryEntity inventoryEntity = getInventory(productId);
        ReserveRequestEntity reserveRequestEntity = getReserveRequest(productId, orderId);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Long quantity = reserveRequestEntity.getQuantity();

        InventoryEntity saved = releaseInventory(inventoryEntity, quantity, currentDateTime);

        reserveRequestEntity.setStatus(ReserveRequestStatus.RELEASED.name());
        reserveRequestRepository.save(reserveRequestEntity);

        InventoryHistoryEntity inventoryHistoryEntity = new InventoryHistoryEntity();
        inventoryHistoryEntity.setProductId(productId);
        inventoryHistoryEntity.setOrderId(orderId);
        inventoryHistoryEntity.setQuantity(quantity);
        inventoryHistoryEntity.setChangeType(InventoryChangeType.RELEASE.name());
        inventoryHistoryEntity.setCreationDateTime(currentDateTime);

        inventoryHistoryRepository.save(inventoryHistoryEntity);

        return inventoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public List<InventoryDto> releaseOrder(String orderId) {
        List<ReserveRequestEntity> reserveRequests = reserveRequestRepository.findAllByOrderId(orderId);

        LocalDateTime currentDateTime = LocalDateTime.now();

        List<InventoryDto> result = new ArrayList<>();

        for (var reserveRequest : reserveRequests) {
            InventoryEntity inventoryEntity = getInventory(reserveRequest.getProductId());
            Long quantity = reserveRequest.getQuantity();
            InventoryEntity saved = releaseInventory(inventoryEntity, quantity, currentDateTime);

            InventoryHistoryEntity inventoryHistoryEntity = new InventoryHistoryEntity();
            inventoryHistoryEntity.setProductId(reserveRequest.getProductId());
            inventoryHistoryEntity.setOrderId(orderId);
            inventoryHistoryEntity.setQuantity(quantity);
            inventoryHistoryEntity.setChangeType(InventoryChangeType.RELEASE.name());
            inventoryHistoryEntity.setCreationDateTime(currentDateTime);

            inventoryHistoryRepository.save(inventoryHistoryEntity);

            result.add(inventoryMapper.toDto(saved));
        }

        return result;
    }

    private InventoryEntity releaseInventory(InventoryEntity inventoryEntity, Long quantity, LocalDateTime currentDateTime) {
        inventoryEntity.setReservedQuantity(inventoryEntity.getReservedQuantity() - quantity);
        inventoryEntity.setLastUpdateDateTime(currentDateTime);
        InventoryEntity saved = inventoryRepository.save(inventoryEntity);
        return saved;
    }

    @Override
    public InventoryDto cancel(InventoryChangeRequest releaseRequest) {
        String productId = releaseRequest.getProductId();
        String orderId = releaseRequest.getOrderId();
        InventoryEntity inventoryEntity = getInventory(productId);
        ReserveRequestEntity reserveRequestEntity = getReserveRequest(productId, orderId);

        Long quantity = reserveRequestEntity.getQuantity();
        LocalDateTime currentDateTime = LocalDateTime.now();

        inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() + quantity);
        inventoryEntity.setReservedQuantity(inventoryEntity.getReservedQuantity() - quantity);
        inventoryEntity.setLastUpdateDateTime(currentDateTime);
        InventoryEntity saved = inventoryRepository.save(inventoryEntity);

        reserveRequestEntity.setStatus(ReserveRequestStatus.CANCELED.name());
        reserveRequestRepository.save(reserveRequestEntity);

        return inventoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public InventoryDto restock(InventoryRestockRequest restockRequest) {
        String productId = restockRequest.getProductId();
        Long quantity = restockRequest.getQuantity();
        InventoryEntity inventoryEntity = getInventory(productId);

        LocalDateTime currentDateTime = LocalDateTime.now();

        inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() + quantity);
        inventoryEntity.setLastUpdateDateTime(currentDateTime);
        InventoryEntity saved = inventoryRepository.save(inventoryEntity);

        InventoryHistoryEntity inventoryHistoryEntity = new InventoryHistoryEntity();
        inventoryHistoryEntity.setProductId(productId);
        inventoryHistoryEntity.setQuantity(quantity);
        inventoryHistoryEntity.setChangeType(InventoryChangeType.RESTOCK.name());
        inventoryHistoryEntity.setCreationDateTime(currentDateTime);

        inventoryHistoryRepository.save(inventoryHistoryEntity);

        return inventoryMapper.toDto(saved);
    }

    @Override
    public InventoryDto createInventory(String productId, LocalDateTime localDateTime) {
        InventoryEntity inventory = new InventoryEntity();
        inventory.setProductId(productId);
        inventory.setAvailableQuantity(0L);
        inventory.setReservedQuantity(0L);
        inventory.setLastUpdateDateTime(localDateTime);
        InventoryEntity save = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(save);
    }

    private InventoryEntity getInventory(String productId) {
        return inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new EntityNotFoundException("Inventory with id %s not found".formatted(productId)));
    }

    private ReserveRequestEntity getReserveRequest(String productId, String orderId) {
        return reserveRequestRepository.findByProductIdAndOrderId(productId, orderId)
                .orElseThrow(() -> new EntityNotFoundException("Reserve request with id " + productId + " not found"));
    }
}
