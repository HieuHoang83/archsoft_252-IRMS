package com.irms.kitchen.domain;

import com.irms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kitchen_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitchenTicket extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "table_id")
    private UUID tableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @Column(name = "expected_ready_time")
    private LocalDateTime expectedReadyTime;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<KitchenTicketItem> items = new ArrayList<>();

    public void addItem(KitchenTicketItem item) {
        items.add(item);
        item.setTicket(this);
    }

    public void removeItem(KitchenTicketItem item) {
        items.remove(item);
        item.setTicket(null);
    }
}
