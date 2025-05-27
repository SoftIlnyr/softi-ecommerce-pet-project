package com.softi.inventoryservice.repository;

import com.softi.inventoryservice.entity.ReserveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReserveRequestRepository extends JpaRepository<ReserveRequestEntity, Long> {

    Optional<ReserveRequestEntity> findByProductIdAndOrderId(String productId, String orderId);

    List<ReserveRequestEntity> findAllByOrderId(String orderId);

}
