package com.irms.kitchen.dto;

import com.irms.kitchen.domain.TicketStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class KitchenTicketDto {
    private UUID id;
    private UUID orderId;
    private UUID tableId;
    private TicketStatus status;
    private LocalDateTime expectedReadyTime;
    private LocalDateTime createdAt;
    private List<KitchenTicketItemDto> items;
    private boolean isBreached;
    private boolean isAtRisk;
}
