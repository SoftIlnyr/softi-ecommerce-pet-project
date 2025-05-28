package com.softi.inventoryservice.mapper;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.dto.InventoryReserveRequest;
import com.softi.inventoryservice.entity.InventoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryDto toDto(InventoryEntity inventoryEntity);

    List<InventoryDto> toDto(List<InventoryEntity> result);
}
