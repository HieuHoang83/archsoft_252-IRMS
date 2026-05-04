package com.irms.kitchen.repository;

import com.irms.kitchen.domain.KitchenTicket;
import com.irms.kitchen.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KitchenTicketRepository extends JpaRepository<KitchenTicket, UUID> {
    List<KitchenTicket> findByStatus(TicketStatus status);
    Optional<KitchenTicket> findByOrderId(UUID orderId);
    List<KitchenTicket> findByStatusInOrderByCreatedAtAsc(List<TicketStatus> statuses);
}
