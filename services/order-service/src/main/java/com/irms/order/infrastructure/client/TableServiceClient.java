package com.irms.order.infrastructure.client;

import com.irms.order.dto.TableResponseDTO;
import java.util.UUID;

public interface TableServiceClient {
    TableResponseDTO getTable(UUID tableId);
}
