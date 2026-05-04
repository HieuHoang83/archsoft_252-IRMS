package com.irms.order.infrastructure.client;

import com.irms.order.dto.MenuItemDTO;
import java.util.UUID;

public interface MenuServiceClient {
    MenuItemDTO getMenuItem(UUID menuItemId);
}
