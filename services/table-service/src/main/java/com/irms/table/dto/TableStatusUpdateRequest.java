package com.irms.table.dto;

import com.irms.table.domain.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TableStatusUpdateRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private TableStatus status;

    // ID đơn hàng hiện tại (truyền khi status = OCCUPIED, null khi giải phóng bàn)
    private UUID currentOrderId;
}
