package com.irms.order.service;

import com.irms.order.domain.Order;
import com.irms.order.domain.OrderItem;
import com.irms.order.domain.OrderItemStatus;
import com.irms.order.dto.OrderItemRequestDTO;
import com.irms.order.dto.OrderItemResponseDTO;
import com.irms.order.exception.BusinessValidationException;
import com.irms.order.exception.InvalidStateTransitionException;
import com.irms.order.exception.OrderNotFoundException;
import com.irms.order.mapper.OrderMapper;
import com.irms.order.repository.OrderItemRepository;
import com.irms.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@SuppressWarnings("null")
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, 
                                OrderRepository orderRepository,
                                OrderMapper orderMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderItemResponseDTO updateOrderItem(UUID itemId, OrderItemRequestDTO itemDTO) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new OrderNotFoundException("Order Item not found"));
        
        if (item.getStatus() != OrderItemStatus.PENDING) {
            throw new BusinessValidationException("Cannot update item that is already being cooked or served.");
        }

        item.setQuantity(itemDTO.getQuantity());
        item.setNote(itemDTO.getNote());
        
        OrderItem savedItem = orderItemRepository.save(item);
        recalculateOrderTotal(item.getOrder());
        
        return orderMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public OrderItemResponseDTO updateOrderItemStatus(UUID itemId, OrderItemStatus newStatus) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new OrderNotFoundException("Order Item not found"));
        
        OrderItemStatus currentStatus = item.getStatus();
        
        // Simple state machine for Order Item
        boolean isValid = switch (currentStatus) {
            case PENDING -> newStatus == OrderItemStatus.COOKING || newStatus == OrderItemStatus.CANCELLED;
            case COOKING -> newStatus == OrderItemStatus.READY_TO_SERVE;
            case READY_TO_SERVE -> newStatus == OrderItemStatus.SERVED;
            case SERVED, CANCELLED -> false;
        };

        if (!isValid) {
            throw new InvalidStateTransitionException("Cannot transition item from " + currentStatus + " to " + newStatus);
        }

        item.setStatus(newStatus);
        OrderItem savedItem = orderItemRepository.save(item);
        
        // If cancelled, recalculate total
        if (newStatus == OrderItemStatus.CANCELLED) {
            recalculateOrderTotal(item.getOrder());
        }
        
        return orderMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public void deleteOrderItem(UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new OrderNotFoundException("Order Item not found"));
        
        if (item.getStatus() != OrderItemStatus.PENDING) {
            throw new BusinessValidationException("Cannot delete item that is already being cooked or served.");
        }
        
        Order order = item.getOrder();
        order.removeItem(item);
        
        orderItemRepository.delete(item);
        recalculateOrderTotal(order);
    }

    private void recalculateOrderTotal(Order order) {
        BigDecimal totalAmount = order.getItems().stream()
                .filter(i -> i.getStatus() != OrderItemStatus.CANCELLED)
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
    }
}
