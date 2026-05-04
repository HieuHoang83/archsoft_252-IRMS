package com.irms.kitchen.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreateTicketRequest {
    @NotNull(message = "Order ID is required")
    private UUID orderId;

    private UUID tableId;

    @NotEmpty(message = "Ticket items cannot be empty")
    private List<CreateTicketItemRequest> items;
}
