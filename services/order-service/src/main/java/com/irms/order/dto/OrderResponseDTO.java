package com.irms.order.dto;

import com.irms.order.domain.OrderStatus;
import com.irms.order.domain.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID id;
    private UUID tableId;
    private UUID waiterId;
    private OrderStatus status;
    private OrderType type;
    private BigDecimal totalAmount;
    private String specialNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponseDTO> items;
}
