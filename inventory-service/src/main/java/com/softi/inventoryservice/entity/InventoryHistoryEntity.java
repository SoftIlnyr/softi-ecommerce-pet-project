package com.softi.inventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory_histories")
public class InventoryHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_histories_id")
    private long id;

    @Column(name = "productId", nullable = false)
    private String productId;

    //todo enum
    @Column(name = "changeType", nullable = false)
    private String changeType;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "created_at")
    private LocalDateTime creationDateTime;

}
