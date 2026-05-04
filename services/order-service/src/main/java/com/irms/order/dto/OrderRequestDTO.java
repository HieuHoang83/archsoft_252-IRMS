package com.irms.order.dto;

import com.irms.order.domain.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private UUID tableId; // Nullable for delivery

    private UUID waiterId;

    @NotNull(message = "Order type is required")
    private OrderType type;

    private String specialNote;

    @Valid
    private List<OrderItemRequestDTO> items;
}
