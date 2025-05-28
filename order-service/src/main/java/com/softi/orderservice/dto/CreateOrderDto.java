package com.softi.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {

    private List<OrderItemDto> items;

}
