package com.irms.order.mapper;

import com.irms.order.domain.Order;
import com.irms.order.domain.OrderItem;
import com.irms.order.dto.OrderItemResponseDTO;
import com.irms.order.dto.OrderResponseDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDTO toDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponseDTO.builder()
                .id(order.getId())
                .tableId(order.getTableId())
                .waiterId(order.getWaiterId())
                .status(order.getStatus())
                .type(order.getType())
                .totalAmount(order.getTotalAmount())
                .specialNote(order.getSpecialNote())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems() != null ? 
                        order.getItems().stream().map(this::toDto).collect(Collectors.toList()) : null)
                .build();
    }

    public OrderItemResponseDTO toDto(OrderItem item) {
        if (item == null) {
            return null;
        }

        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .menuItemId(item.getMenuItemId())
                .menuItemName(item.getMenuItemName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .note(item.getNote())
                .status(item.getStatus())
                .build();
    }
}
