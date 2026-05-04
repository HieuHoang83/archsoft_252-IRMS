package com.irms.kitchen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateTicketItemRequest {
    @NotNull(message = "Menu item ID is required")
    private UUID menuItemId;

    @NotBlank(message = "Menu item name is required")
    private String menuItemName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String notes;
}
