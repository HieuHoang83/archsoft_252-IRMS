package com.irms.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class KitchenTicketRequestDTO {
    private UUID orderId;
    private UUID tableId;
    private List<KitchenTicketItemRequestDTO> items;
}
