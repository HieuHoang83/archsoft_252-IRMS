package com.irms.order.service;

import com.irms.order.domain.OrderStatus;
import com.irms.order.dto.OrderItemRequestDTO;
import com.irms.order.dto.OrderRequestDTO;
import com.irms.order.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
    OrderResponseDTO getOrder(UUID id);
    Page<OrderResponseDTO> getOrders(OrderStatus status, UUID waiterId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<OrderResponseDTO> getKitchenOrders();
    OrderResponseDTO updateOrderStatus(UUID id, OrderStatus newStatus);
    void deleteOrder(UUID id);
    
    // Order Item specific operations handled here for convenience or in OrderItemService
    OrderResponseDTO addOrderItem(UUID orderId, OrderItemRequestDTO itemDTO);
}
