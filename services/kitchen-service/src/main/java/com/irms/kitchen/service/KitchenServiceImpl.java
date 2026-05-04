package com.irms.kitchen.service;

import com.irms.kitchen.domain.KitchenTicket;
import com.irms.kitchen.domain.KitchenTicketItem;
import com.irms.kitchen.domain.StationType;
import com.irms.kitchen.domain.TicketItemStatus;
import com.irms.kitchen.domain.TicketStatus;
import com.irms.kitchen.dto.CreateTicketRequest;
import com.irms.kitchen.exception.ResourceNotFoundException;
import com.irms.kitchen.repository.KitchenTicketItemRepository;
import com.irms.kitchen.repository.KitchenTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class KitchenServiceImpl implements KitchenService {

    private final KitchenTicketRepository ticketRepository;
    private final KitchenTicketItemRepository itemRepository;
    private final StationManager stationManager;
    private final OrderPrioritizer orderPrioritizer;

    @Override
    @Transactional
    public KitchenTicket createTicket(CreateTicketRequest request) {
        log.info("Creating kitchen ticket for order ID: {}", request.getOrderId());
        
        KitchenTicket ticket = KitchenTicket.builder()
                .orderId(request.getOrderId())
                .tableId(request.getTableId())
                .status(TicketStatus.PENDING)
                .build();
                
        // Calculate expected ready time based on prioritizer
        ticket.setExpectedReadyTime(orderPrioritizer.calculateExpectedReadyTime(ticket));

        request.getItems().forEach(itemDto -> {
            StationType station = stationManager.determineStation(itemDto.getMenuItemId(), itemDto.getMenuItemName());
            KitchenTicketItem item = KitchenTicketItem.builder()
                    .menuItemId(itemDto.getMenuItemId())
                    .menuItemName(itemDto.getMenuItemName())
                    .quantity(itemDto.getQuantity())
                    .notes(itemDto.getNotes())
                    .station(station)
                    .status(TicketItemStatus.PENDING)
                    .build();
            ticket.addItem(item);
        });

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KitchenTicket> getActiveTickets() {
        return ticketRepository.findByStatusInOrderByCreatedAtAsc(
                Arrays.asList(TicketStatus.PENDING, TicketStatus.PREPARING)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public KitchenTicket getTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen ticket not found with id: " + ticketId));
    }

    @Override
    @Transactional
    public void updateItemStatus(UUID itemId, TicketItemStatus newStatus) {
        log.info("Updating status for item ID: {} to {}", itemId, newStatus);
        KitchenTicketItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen ticket item not found with id: " + itemId));
                
        item.setStatus(newStatus);
        itemRepository.save(item);
        
        // Auto-update ticket status based on items
        checkAndUpdateTicketStatus(item.getTicket());
        
        // TODO: Publish event to Message Queue (e.g., OrderReady if ticket completed)
    }

    @Override
    @Transactional
    public void updateTicketStatus(UUID ticketId, TicketStatus newStatus) {
        log.info("Updating status for ticket ID: {} to {}", ticketId, newStatus);
        KitchenTicket ticket = getTicketById(ticketId);
        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);
        
        // TODO: If cancelled, maybe cascade cancel to items
    }

    @Override
    @Transactional(readOnly = true)
    public List<KitchenTicketItem> getItemsByStation(StationType station) {
        return itemRepository.findByStationAndStatusInOrderByCreatedAtAsc(
                station, 
                Arrays.asList(TicketItemStatus.PENDING, TicketItemStatus.COOKING)
        );
    }
    
    private void checkAndUpdateTicketStatus(KitchenTicket ticket) {
        List<KitchenTicketItem> items = ticket.getItems();
        
        boolean allReady = items.stream().allMatch(i -> i.getStatus() == TicketItemStatus.READY || i.getStatus() == TicketItemStatus.CANCELLED);
        boolean anyCooking = items.stream().anyMatch(i -> i.getStatus() == TicketItemStatus.COOKING);
        
        if (allReady) {
            ticket.setStatus(TicketStatus.READY_TO_SERVE);
            log.info("Ticket {} is now READY_TO_SERVE", ticket.getId());
        } else if (anyCooking && ticket.getStatus() == TicketStatus.PENDING) {
            ticket.setStatus(TicketStatus.PREPARING);
            log.info("Ticket {} is now PREPARING", ticket.getId());
        }
        
        ticketRepository.save(ticket);
    }
}
