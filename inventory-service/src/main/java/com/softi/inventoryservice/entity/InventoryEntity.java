package com.softi.inventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table(name = "inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "productId", nullable = false)
    private String productId;

    @Column(name = "availableQuantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "reservedQuantity", nullable = false)
    private Integer reservedQuantity;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdateDateTime;

}
