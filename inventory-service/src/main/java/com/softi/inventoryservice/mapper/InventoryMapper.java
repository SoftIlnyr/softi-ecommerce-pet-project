package com.softi.inventoryservice.mapper;

import com.softi.inventoryservice.dto.InventoryDto;
import com.softi.inventoryservice.entity.InventoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryDto toDto(InventoryEntity inventoryEntity);

}
