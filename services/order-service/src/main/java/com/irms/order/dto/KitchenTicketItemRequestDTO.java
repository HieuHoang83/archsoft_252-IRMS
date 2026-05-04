package com.irms.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class KitchenTicketItemRequestDTO {
    private UUID menuItemId;
    private String menuItemName;
    private Integer quantity;
    private String notes;
}
