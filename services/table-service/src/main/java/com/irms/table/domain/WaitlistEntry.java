package com.irms.table.domain;

import com.irms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitlistEntry extends BaseEntity {

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "party_size", nullable = false)
    private Integer partySize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private WaitlistStatus status = WaitlistStatus.WAITING;

    // Thời gian chờ ước tính (phút), được tính toán lại định kỳ
    @Column(name = "estimated_wait_minutes")
    private Integer estimatedWaitMinutes;

    // Thời điểm gửi thông báo "bàn sẵn sàng"
    @Column(name = "notified_at")
    private LocalDateTime notifiedAt;

    // Vị trí trong hàng đợi (tính từ 1)
    @Column(name = "queue_position")
    private Integer queuePosition;
}
