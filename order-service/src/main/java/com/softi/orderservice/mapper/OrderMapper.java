package com.softi.orderservice.mapper;

import com.softi.orderservice.dto.CreateOrderDto;
import com.softi.orderservice.dto.OrderDto;
import com.softi.orderservice.dto.OrderItemDto;
import com.softi.orderservice.models.Order;
import com.softi.orderservice.models.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);

    List<OrderDto> toOrderDtoList(List<Order> orders);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    OrderItem toOrderItem(OrderItemDto orderItemDto);

    List<OrderItem> toOrderItemDtoList(List<OrderItemDto> orderItems);

    List<OrderItemDto> toOrderItemList(List<OrderItem> orderItems);

    Order toOrder(CreateOrderDto createOrderDto);
}
