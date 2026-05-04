package com.irms.kitchen.dto;

import com.irms.kitchen.domain.TicketItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateItemStatusRequest {
    @NotNull(message = "Status is required")
    private TicketItemStatus status;
}
