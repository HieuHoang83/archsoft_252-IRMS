package com.irms.table.repository;

import com.irms.table.domain.WaitlistEntry;
import com.irms.table.domain.WaitlistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WaitlistRepository extends JpaRepository<WaitlistEntry, UUID> {

    List<WaitlistEntry> findByStatusOrderByCreatedAtAsc(WaitlistStatus status);

    List<WaitlistEntry> findByStatusInOrderByCreatedAtAsc(List<WaitlistStatus> statuses);

    long countByStatus(WaitlistStatus status);
}
