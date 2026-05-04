package com.irms.order.controller;

import com.irms.order.domain.OrderItemStatus;
import com.irms.order.domain.OrderStatus;
import com.irms.order.dto.OrderItemRequestDTO;
import com.irms.order.dto.OrderItemResponseDTO;
import com.irms.order.dto.OrderRequestDTO;
import com.irms.order.dto.OrderResponseDTO;
import com.irms.order.service.OrderItemService;
import com.irms.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(requestDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) UUID waiterId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<OrderResponseDTO> orders = orderService.getOrders(status, waiterId, startDate, endDate, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/kitchen")
    public ResponseEntity<List<OrderResponseDTO>> getKitchenOrders() {
        List<OrderResponseDTO> kitchenOrders = orderService.getKitchenOrders();
        return ResponseEntity.ok(kitchenOrders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // --- Order Item Endpoints ---

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderResponseDTO> addOrderItem(
            @PathVariable UUID id,
            @Valid @RequestBody OrderItemRequestDTO itemDTO) {
        OrderResponseDTO updatedOrder = orderService.addOrderItem(id, itemDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId,
            @Valid @RequestBody OrderItemRequestDTO itemDTO) {
        OrderItemResponseDTO updatedItem = orderItemService.updateOrderItem(itemId, itemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @PutMapping("/{id}/items/{itemId}/status")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItemStatus(
            @PathVariable UUID id,
            @PathVariable UUID itemId,
            @RequestParam OrderItemStatus status) {
        OrderItemResponseDTO updatedItem = orderItemService.updateOrderItemStatus(itemId, status);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId) {
        orderItemService.deleteOrderItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
