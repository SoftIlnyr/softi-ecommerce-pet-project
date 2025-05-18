package com.softi.orderservice.dto;

import com.softi.orderservice.models.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String id;
    private String customerId;
    private String status;
    private List<OrderItemDto> items;

}
