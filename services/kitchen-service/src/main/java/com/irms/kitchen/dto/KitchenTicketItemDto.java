package com.irms.kitchen.dto;

import com.irms.kitchen.domain.StationType;
import com.irms.kitchen.domain.TicketItemStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class KitchenTicketItemDto {
    private UUID id;
    private UUID ticketId;
    private UUID menuItemId;
    private String menuItemName;
    private Integer quantity;
    private String notes;
    private StationType station;
    private TicketItemStatus status;
    private LocalDateTime createdAt;
}
