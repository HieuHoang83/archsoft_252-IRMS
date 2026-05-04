package com.irms.order.dto;

import com.irms.order.domain.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private UUID id;
    private UUID menuItemId;
    private String menuItemName;
    private Integer quantity;
    private BigDecimal price;
    private String note;
    private OrderItemStatus status;
}
