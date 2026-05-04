package com.irms.table.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SeatGuestRequest {

    @NotNull(message = "ID bàn không được để trống")
    private UUID tableId;

    // Nguồn khách: "RESERVATION" hoặc "WAITLIST" hoặc "WALK_IN"
    private String source;

    // ID của Reservation hoặc WaitlistEntry tương ứng (nullable nếu source = WALK_IN)
    private UUID sourceId;
}
