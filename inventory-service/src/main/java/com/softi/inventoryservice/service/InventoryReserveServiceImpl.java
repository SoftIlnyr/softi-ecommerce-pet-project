package com.softi.inventoryservice.service;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.exception.InsufficientStockException;
import com.softi.inventoryservice.kafka.InventoryKafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class InventoryReserveServiceImpl implements InventoryReserveService {

    private final InventoryService inventoryService;
    private final InventoryKafkaProducerService inventoryKafkaProducerService;

    @Transactional
    @Override
    public InventoryReserveResult reserveAll(List<InventoryReserveRequest> reserveRequests) {
        List<String> reservedInventories = new ArrayList<>();
        List<String> cancelledInventories = new ArrayList<>();
        for (InventoryReserveRequest reserveRequest : reserveRequests) {
            try {
                var reserveResult = inventoryService.reserve(reserveRequest);
                reservedInventories.add(reserveResult.getProductId());
            } catch (InsufficientStockException | ObjectOptimisticLockingFailureException e) {
                log.error("Could not reserve inventory", e);
                cancelledInventories.add(reserveRequest.getProductId());
            }
        }
        String orderId = reserveRequests.stream().findFirst().map(InventoryReserveRequest::getOrderId)
                .orElseThrow(() -> new IllegalStateException("Order id not found"));
        InventoryReserveResult reserveResult = new InventoryReserveResult(orderId, reservedInventories, cancelledInventories);
        inventoryKafkaProducerService.createInventoryReservedEvent(reserveResult);
        return reserveResult;
    }
}
